package io.craigmiller160.oauth2.exception

import java.lang.RuntimeException

// TODO add to Spring exception handler
class BadAuthCodeRequestException(msg: String) : RuntimeException(msg)