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

class UserParsingFilter(
  private val order: Int,
  private val jwtUtil: JwtUtil
) : Filter, Ordered {

  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val req = request as HttpServletRequest
    val authHeader = req.getHeaders(HttpHeaders.AUTHORIZATION).iterator().asSequence()
      .firstOrNull { jwtUtil.verifySchema(it) }
    if (authHeader == null) chain.doFilter(request, response)
    else {
      UserHolder.jwtAuthHeader.set(authHeader)
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
      try {
        chain.doFilter(request, response)
      } finally {
        UserHolder.jwtAuthHeader.set("")
        UserHolder.user.set(Anon)
      }
    }
  }

  override fun getOrder(): Int = order
}