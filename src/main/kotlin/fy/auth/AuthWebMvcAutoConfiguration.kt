package fy.auth

import ez.jwt.JwtAutoConfiguration
import ez.jwt.JwtUtil
import fy.auth.sak.AuthServiceApiKeyAutoConfiguration
import fy.auth.sak.ServiceApiKey
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@ConfigurationProperties("fy.auth.web-mvc")
@Import(value = [JwtAutoConfiguration::class, AuthServiceApiKeyAutoConfiguration::class])
@Configuration
class AuthWebMvcAutoConfiguration {
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

  @Bean
  fun userParsingFilter(jwtUtil: JwtUtil) = UserParsingFilter(userParsingFilterOrder, jwtUtil)

  @Bean
  fun authInterceptor(serviceApiKey: ServiceApiKey) =
    AuthInterceptor(authInterceptorOrder, serviceApiKey)
}