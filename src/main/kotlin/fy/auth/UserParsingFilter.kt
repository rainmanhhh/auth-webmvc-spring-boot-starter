package fy.auth

import ez.jwt.Anon
import ez.jwt.JwtUtil
import fy.auth.user.UserHolder
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * if there's a valid jwtToken in request, save it(and parsed [ez.jwt.JwtUser]) to [UserHolder]
 */
class UserParsingFilter(
  private val order: Int,
  private val jwtUtil: JwtUtil
) : Filter, Ordered {

  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val req = request as HttpServletRequest
    val jwtToken = Iterable {
      req.getHeaders(HttpHeaders.AUTHORIZATION).iterator()
    }.firstOrNull {
      jwtUtil.verifySchema(it)
    }
    if (jwtToken == null) chain.doFilter(request, response)
    else {
      val user = try {
        jwtUtil.verifyAuthHeader(Iterable {
          req.getHeaders(HttpHeaders.AUTHORIZATION).asIterator()
        })
      } catch (e: ExpiredJwtException) {
        throw ResponseStatusException(
          HttpStatus.UNAUTHORIZED,
          e.javaClass.name + ':' + e.message,
          e
        )
      }
      UserHolder.user.set(user)
      UserHolder.jwtToken.set(jwtToken)
      try {
        chain.doFilter(request, response)
      } finally {
        UserHolder.user.set(Anon)
        UserHolder.jwtToken.set("")
      }
    }
  }

  override fun getOrder(): Int = order
}