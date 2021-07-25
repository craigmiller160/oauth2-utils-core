package io.craigmiller160.oauth2.security

import com.nimbusds.jwt.JWTClaimsSet

interface RequestWrapper {
    fun getCookieValue(cookieName: String): String?
    fun getHeaderValue(headerName: String): String?
    fun setAuthentication(claims: JWTClaimsSet)
    fun setNewTokenCookie(cookie: String)
    fun getRequestUri(): String
}
