package fy.auth

import org.springframework.util.DigestUtils

class ServiceApiKey {
  /**
   * SAK http header name
   */
  var name = "X-SAK"

  /**
   * SAK secret key
   */
  var value = ""

  /**
   * SAK salt, default: random md5 hex(32 chars, lower case), regenerate every time app starts
   */
  var salt = DigestUtils.md5DigestAsHex(Math.random().toString().toByteArray())

  /**
   * encode SAK. format: MD5([value]+[salt]).toHex() + [salt]
   * @return if name or value is empty return null; else return encoded SAK
   */
  fun encode(): String? {
    if (name.isEmpty() || value.isEmpty()) return null
    val hex = DigestUtils.md5DigestAsHex((value + salt).toByteArray())
    return hex + salt
  }
}