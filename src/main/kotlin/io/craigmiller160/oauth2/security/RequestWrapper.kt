package io.craigmiller160.oauth2.security

interface RequestWrapper {
    fun getCookieToken(cookieName: String): String
    fun getBearerToken(): String
}
