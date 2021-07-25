package io.craigmiller160.oauth2.security.impl

import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.exception.InvalidTokenException
import io.craigmiller160.oauth2.security.AuthenticationFilterService
import io.craigmiller160.oauth2.security.RequestWrapper

class AuthenticationFilterServiceImpl(
        private val oAuth2Config: OAuth2Config
) : AuthenticationFilterService {
    override fun authenticateRequest(req: RequestWrapper) {
        runCatching {
            val token = getToken(req)
        }
    }

    private fun getToken(req: RequestWrapper): String {
        return req.getBearerToken()
                ?: req.getCookieToken(oAuth2Config.cookieName)
                ?: throw InvalidTokenException("Token not found")
    }
}