package fy.auth

import ez.jwt.isAnon
import feign.RequestInterceptor
import feign.RequestTemplate
import fy.auth.user.UserHolder
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders

class AuthInterceptor(
  private val order: Int,
  private val serviceApiKeyName: String,
  private val serviceApiKeyValue: String
) : RequestInterceptor, Ordered {
  override fun getOrder(): Int = order

  override fun apply(template: RequestTemplate) {
    template.header(serviceApiKeyName, serviceApiKeyValue)
    val jwtAuthHeader = UserHolder.jwtAuthHeader.get()
    val user = UserHolder.user.get()
    if (!user.isAnon) template.header(HttpHeaders.AUTHORIZATION, jwtAuthHeader)
  }
}