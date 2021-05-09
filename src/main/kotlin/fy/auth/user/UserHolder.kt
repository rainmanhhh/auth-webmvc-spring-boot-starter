package fy.auth.user

import ez.jwt.Anon

object UserHolder {
  val jwtAuthHeader = ThreadLocal.withInitial { "" }
  val user = ThreadLocal.withInitial { Anon }
}
