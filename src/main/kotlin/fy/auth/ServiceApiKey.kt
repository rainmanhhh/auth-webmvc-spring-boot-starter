package fy.auth

import org.slf4j.LoggerFactory
import org.springframework.util.DigestUtils

class ServiceApiKey {
  companion object {
    private val logger = LoggerFactory.getLogger(ServiceApiKey::class.java)
  }
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

  fun isConfigValid() = name.isNotEmpty() && value.isNotEmpty()

  /**
   * encode SAK. format: MD5([value]+[salt]).toHex() + [salt]
   * @return if name or value is empty return null; else return encoded SAK
   */
  fun encode(): String? {
    return if (isConfigValid()) {
      val hex = DigestUtils.md5DigestAsHex((value + salt).toByteArray())
      hex + salt
    } else {
      logger.warn("SAK config not valid(name or value is empty)")
      null
    }
  }

  /**
   * check if the input key match with local key
   */
  fun check(inputKey: String?): Boolean {
    return if (inputKey == null) {
      logger.debug("inputKey is null")
      false
    } else {
      val localKey = encode()
      val result = inputKey == localKey
      if (!result) logger.warn("SAK not match! inputKey: {}, localKey: {}", inputKey, localKey)
      result
    }
  }
}