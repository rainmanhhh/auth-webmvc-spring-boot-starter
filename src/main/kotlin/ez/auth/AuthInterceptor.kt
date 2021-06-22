package ez.auth

import ez.jwt.isAnon
import feign.RequestInterceptor
import feign.RequestTemplate
import ez.auth.sak.ServiceApiKey
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders

/**
 * 1. if SAK name and value are not empty, add SAK to request header. format is `MD5(value + salt) + salt`
 * 2. if there's a valid [ez.jwt.JwtUser] saved in [UserHolder], add it's token to request header
 */
class AuthInterceptor(
  private val order: Int,
  private val serviceApiKey: ServiceApiKey
) : RequestInterceptor, Ordered {
  override fun getOrder(): Int = order

  override fun apply(template: RequestTemplate) {
    // add SAK to request
    serviceApiKey.encodeLocal()?.let { template.header(serviceApiKey.name, it) }
    // add jwt token to request
    val jwtToken = UserHolder.jwtToken.get()
    val user = UserHolder.user.get()
    if (!user.isAnon) template.header(HttpHeaders.AUTHORIZATION, jwtToken)
  }
}