package io.craigmiller160.oauth2.service

import io.craigmiller160.oauth2.dto.TokenResponseDto

interface RefreshTokenService {
    fun refreshToken(accessToken: String): TokenResponseDto?
}