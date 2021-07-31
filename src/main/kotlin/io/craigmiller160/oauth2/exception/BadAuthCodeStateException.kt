package io.craigmiller160.oauth2.exception

import java.lang.RuntimeException

class BadAuthCodeStateException(msg: String) : OAuth2Exception(msg) {
    override val statusCode: Int = 403
}