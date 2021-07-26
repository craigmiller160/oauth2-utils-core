package io.craigmiller160.oauth2.exception

import java.lang.RuntimeException

class BadAuthCodeRequestException(msg: String) : OAuth2Exception(msg)