package fy.auth.user

import ez.jwt.Anon
import ez.jwt.JwtUser

object UserHolder {
  val jwtToken: ThreadLocal<String> = ThreadLocal.withInitial { "" }
  val user: ThreadLocal<JwtUser> = ThreadLocal.withInitial { Anon }
}
