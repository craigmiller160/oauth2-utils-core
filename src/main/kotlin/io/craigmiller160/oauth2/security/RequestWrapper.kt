package io.craigmiller160.oauth2.security

import com.nimbusds.jwt.JWTClaimsSet

interface RequestWrapper {
    fun getCookieToken(cookieName: String): String? // TODO replace with get cookie value
    fun getBearerToken(): String? // TODO replace with get header value
    fun setAuthentication(claims: JWTClaimsSet)
    fun setNewTokenCookie(cookie: String)
}
