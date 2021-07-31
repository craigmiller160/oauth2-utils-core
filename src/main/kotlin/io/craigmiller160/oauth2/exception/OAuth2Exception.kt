package io.craigmiller160.oauth2.exception

import java.lang.RuntimeException

abstract class OAuth2Exception(msg: String, cause: Throwable? = null) : RuntimeException(msg, cause) {
    abstract val statusCode: Int
}