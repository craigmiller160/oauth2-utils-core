package io.craigmiller160.oauth2.security

import com.nimbusds.jwt.JWTClaimsSet

interface RequestWrapper {
    fun getCookieToken(cookieName: String): String?
    fun getBearerToken(): String?
    fun setAuthentication(claims: JWTClaimsSet)
    fun setNewTokenCookie(cookie: String)
}
