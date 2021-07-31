package io.craigmiller160.oauth2.exception

import java.lang.RuntimeException

class InvalidTokenException(msg: String, cause: Throwable? = null) : OAuth2Exception(msg, cause) {
    override val statusCode: Int = 401
}