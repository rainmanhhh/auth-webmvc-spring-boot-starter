package fy.auth.user

enum class UserType {
  /**
   * auth.user
   */
  AuthUser,

  /**
   * zsfy pub app user
   */
  PubUser,

  /**
   * staff
   */
  Staff,

  /**
   * use phone as user id(suggest to give user roles or perms with this type instead of PubUser or Staff)
   */
  Phone
}
