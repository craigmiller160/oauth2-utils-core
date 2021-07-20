package io.craigmiller160.oauth2.exception

import java.lang.RuntimeException

// TODO add to Spring exception handler
class BadAuthCodeStateException(msg: String) : RuntimeException(msg)