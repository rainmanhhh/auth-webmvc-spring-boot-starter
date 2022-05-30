package ez.auth

import ez.auth.sak.AuthServiceApiKeyAutoConfiguration
import ez.auth.sak.ServiceApiKey
import ez.jwt.JwtAutoConfiguration
import ez.jwt.JwtUtil
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@ConfigurationProperties("ez.auth.web-mvc")
@Import(value = [JwtAutoConfiguration::class, AuthServiceApiKeyAutoConfiguration::class])
@Configuration
class AuthWebMvcAutoConfiguration {
  /**
   * order of user parsing filter(for spring webmvc)
   */
  var userParsingFilterOrder = 0

  /**
   * order of auth interceptor(for feign client)
   */
  var authInterceptorOrder = 0

  /**
   * if there's no jwt in Authorization header, try to find jwt in cookie by this name
   */
  var cookieName = "JWT"

  @Bean
  fun userParsingFilter(jwtUtil: JwtUtil) = UserParsingFilter(userParsingFilterOrder, jwtUtil, this)

  @Bean
  fun authInterceptor(serviceApiKey: ServiceApiKey) =
    AuthInterceptor(authInterceptorOrder, serviceApiKey)
}