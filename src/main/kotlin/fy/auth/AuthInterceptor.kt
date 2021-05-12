package fy.auth

import ez.jwt.isAnon
import feign.RequestInterceptor
import feign.RequestTemplate
import fy.auth.user.UserHolder
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.util.DigestUtils

/**
 * 1. if SAX name and value are not empty, add SAX to request header. format is `MD5(value + salt) + salt`
 * 2. if there's a valid [ez.jwt.JwtUser] saved in [UserHolder], add it's token to request header
 */
class AuthInterceptor(
  private val order: Int,
  private val serviceApiKey: ServiceApiKey
) : RequestInterceptor, Ordered {
  override fun getOrder(): Int = order

  override fun apply(template: RequestTemplate) {
    // add SAX to request
    if (serviceApiKey.name.isNotEmpty() && serviceApiKey.value.isNotEmpty()) {
      val salt = serviceApiKey.salt
      val hex = DigestUtils.md5DigestAsHex((serviceApiKey.value + salt).toByteArray())
      template.header(serviceApiKey.name, hex + salt)
    }
    // add jwt token to request
    val jwtToken = UserHolder.jwtToken.get()
    val user = UserHolder.user.get()
    if (!user.isAnon) template.header(HttpHeaders.AUTHORIZATION, jwtToken)
  }
}