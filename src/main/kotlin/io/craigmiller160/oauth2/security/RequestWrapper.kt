package io.craigmiller160.oauth2.security

import com.nimbusds.jwt.JWTClaimsSet

data class RequestWrapper(
        val requestUri: String,
        val getCookieValue: (cookieName: String) -> String?,
        val getHeaderValue: (headerName: String) -> String?,
        val setAuthentication: (claims: JWTClaimsSet) -> Unit,
        val setNewTokenCookie: (cookie: String) -> Unit
)