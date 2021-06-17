package io.craigmiller160.oauth2.client

import io.craigmiller160.oauth2.dto.TokenResponseDto

interface AuthServerClient {
    fun authenticateAuthCode(origin: String, code: String): TokenResponseDto
    fun authenticateRefreshToken(refreshToken: String): TokenResponseDto
}