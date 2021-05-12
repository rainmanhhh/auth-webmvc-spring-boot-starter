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
}