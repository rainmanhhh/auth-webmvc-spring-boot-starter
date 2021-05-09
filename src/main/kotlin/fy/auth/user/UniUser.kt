package fy.auth.user

import ez.jwt.JwtUser

fun JwtUser.getIdOrNull(userType: UserType): String? {
  val prefix = "user:$userType:"
  for (role in roles) {
    val userId = role.substringAfter(prefix, "")
    if (userId != "") return userId
  }
  return null
}

fun JwtUser.getId(userType: UserType): String {
  return getIdOrNull(userType) ?: throw IllegalArgumentException("user $id is not $userType")
}
