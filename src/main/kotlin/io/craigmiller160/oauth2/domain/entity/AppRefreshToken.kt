package io.craigmiller160.oauth2.domain.entity

interface AppRefreshToken {
    val id: Long
    val tokenId: String
    val refreshToken: String
}