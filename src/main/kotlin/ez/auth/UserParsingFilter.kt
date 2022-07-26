package ez.auth

import ez.jwt.Anon
import ez.jwt.JwtUtil
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
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
  private val jwtUtil: JwtUtil,
  private val config: AuthWebMvcAutoConfiguration,
) : Filter, Ordered {

  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val req = request as HttpServletRequest
    val jwtToken = req.getHeaders(HttpHeaders.AUTHORIZATION).asSequence().firstOrNull {
      jwtUtil.verifySchema(it)
    } ?: req.cookies?.firstOrNull {
      it.name == config.cookieName
    }?.value?.let {
      val v = URLDecoder.decode(it, StandardCharsets.UTF_8)
      if (jwtUtil.verifySchema(v)) v else null
    }
    if (jwtToken == null) {
      UserHolder.user.set(Anon)
      UserHolder.jwtToken.set("")
      chain.doFilter(request, response)
    } else {
      val user = try {
        jwtUtil.verifyToken(jwtToken)
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