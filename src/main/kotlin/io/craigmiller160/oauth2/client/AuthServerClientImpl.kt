package io.craigmiller160.oauth2.client

import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.dto.TokenResponseDto

class AuthServerClientImpl(
        private val oAuth2Config: OAuth2Config
) : AuthServerClient {
    override fun authenticateAuthCode(origin: String, code: String): TokenResponseDto {
        TODO("Not yet implemented")
    }

    override fun authenticateRefreshToken(refreshToken: String): TokenResponseDto {
        TODO("Not yet implemented")
    }

    private fun tokenRequest(): TokenResponseDto {
        TODO("Finish this")
    }
}