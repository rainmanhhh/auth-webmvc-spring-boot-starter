package ez.auth

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
   * order of user parsing filter(for spring webmvc)
   */
  var userParsingFilterOrder = 0

  /**
   * order of auth interceptor(for feign client)
   */
  var authInterceptorOrder = 0

  @Bean
  fun userParsingFilter(jwtUtil: JwtUtil) = UserParsingFilter(userParsingFilterOrder, jwtUtil)

  @Bean
  fun authInterceptor(serviceApiKey: ServiceApiKey) =
    AuthInterceptor(authInterceptorOrder, serviceApiKey)
}