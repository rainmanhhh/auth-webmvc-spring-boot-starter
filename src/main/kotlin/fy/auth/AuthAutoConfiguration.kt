package fy.auth

import ez.jwt.JwtAutoConfiguration
import ez.jwt.JwtUtil
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

const val prefix = "fy.auth"
const val enabledKey = "$prefix.enable-filter-and-interceptor"

@ConfigurationProperties(prefix)
@Import(JwtAutoConfiguration::class)
@Configuration
class AuthAutoConfiguration {
  /**
   * 1. enable [UserParsingFilter]: saving jwt token(and user info) from incoming request
   * 2. enable [AuthInterceptor]: add jwt token and SAK to outgoing request
   */
  var enableFilterAndInterceptor = true

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
  @NestedConfigurationProperty
  var serviceApiKey = ServiceApiKey()

  @Bean
  fun serviceApiKey() = serviceApiKey

  @ConditionalOnProperty(
    name = [enabledKey],
    havingValue = true.toString(),
    matchIfMissing = true
  )
  @Bean
  fun userParsingFilter(jwtUtil: JwtUtil) = UserParsingFilter(userParsingFilterOrder, jwtUtil)

  @ConditionalOnProperty(
    name = [enabledKey],
    havingValue = true.toString(),
    matchIfMissing = true
  )
  @Bean
  fun authInterceptor(serviceApiKey: ServiceApiKey) = AuthInterceptor(authInterceptorOrder, serviceApiKey)
}