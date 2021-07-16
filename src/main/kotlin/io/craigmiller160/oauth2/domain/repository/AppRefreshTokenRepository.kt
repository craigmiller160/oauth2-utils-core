package io.craigmiller160.oauth2.domain.repository

import io.craigmiller160.oauth2.domain.entity.AppRefreshToken

interface AppRefreshTokenRepository<T : AppRefreshToken> {
    fun findByTokenId(tokenId: String): T
    fun removeByTokenId(tokenId: String): Int
}