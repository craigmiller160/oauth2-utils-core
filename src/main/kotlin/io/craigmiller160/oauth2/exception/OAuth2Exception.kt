package io.craigmiller160.oauth2.exception

import java.lang.RuntimeException

open class OAuth2Exception(msg: String, cause: Throwable? = null) : RuntimeException(msg, cause)