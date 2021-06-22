package ez.auth

import ez.jwt.Anon
import ez.jwt.JwtUser

object UserHolder {
  /**
   * [UserParsingFilter] will save jwt token after parsing incoming request,
   * and [AuthInterceptor] will use it when sending outgoing request
   */
  val jwtToken: ThreadLocal<String> = ThreadLocal.withInitial { "" }

  /**
   * [UserParsingFilter] will save user info after parsing incoming request,
   * and [AuthInterceptor] will add jwt header to outgoing request if the saved user is not anon;
   * developer could also use the saved user info to do something(eg. filter data with user id)
   */
  val user: ThreadLocal<JwtUser> = ThreadLocal.withInitial { Anon }
}
