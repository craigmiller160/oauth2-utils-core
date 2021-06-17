package io.craigmiller160.oauth2.client

import io.craigmiller160.oauth2.dto.TokenResponseDto

class AuthServerClientImpl : AuthServerClient {
    override fun authenticateAuthCode(origin: String, code: String): TokenResponseDto {
        TODO("Not yet implemented")
    }

    override fun authenticateRefreshToken(refreshToken: String): TokenResponseDto {
        TODO("Not yet implemented")
    }
}