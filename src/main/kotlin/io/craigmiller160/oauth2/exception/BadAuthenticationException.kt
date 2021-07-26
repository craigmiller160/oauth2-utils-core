package io.craigmiller160.oauth2.exception

import java.lang.RuntimeException

class BadAuthenticationException(msg: String, cause: Throwable?) : OAuth2Exception(msg, cause) {
    override val statusCode: Int = 401
}