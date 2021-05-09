package fy.auth

import ez.jwt.JwtAutoConfiguration
import ez.jwt.JwtUtil
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@ConfigurationProperties("fy.auth")
@Import(JwtAutoConfiguration::class)
@Configuration
class AuthAutoConfiguration {
  /**
   * user parsing filter order
   */
  var userParsingFilterOrder = 0

  /**
   * auth interceptor order
   */
  var authInterceptorOrder = 0

  /**
   * when access one service from another, if this key matched, allow to access any api without user perm check
   */
  var serviceApiKeyName = "X-SAK"

  /**
   * when access one service from another, if this key matched, allow to access any api without user perm check
   */
  var serviceApiKeyValue = ""

  @Bean
  fun userParsingFilter(jwtUtil: JwtUtil) = UserParsingFilter(userParsingFilterOrder, jwtUtil)

  @Bean
  fun authInterceptor() = AuthInterceptor(authInterceptorOrder, serviceApiKeyName, serviceApiKeyValue)
}