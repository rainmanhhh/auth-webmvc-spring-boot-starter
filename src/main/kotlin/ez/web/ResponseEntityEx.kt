package ez.web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ResponseStatusException

/**
 * check whether the api calling returns 2xx successful code.
 * @return response body
 * @throws [org.springframework.web.server.ResponseStatusException] if server returns non-2xx http code. error message will be like "call [apiName] failed! code: xxx"
 */
fun <T> ResponseEntity<T>.check(apiName: String): T? {
  if (statusCode.is2xxSuccessful) return body
  else throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "call [$apiName] failed! code: $statusCodeValue")
}
