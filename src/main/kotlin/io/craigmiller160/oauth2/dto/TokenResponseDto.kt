package io.craigmiller160.oauth2.dto

data class TokenResponseDto(
        val accessToken: String,
        val refreshToken: String,
        val tokenId: String
)
