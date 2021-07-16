package io.craigmiller160.oauth2.domain.repository

import io.craigmiller160.oauth2.domain.entity.AppRefreshToken

interface AppRefreshTokenRepository {
    fun findByTokenId(tokenId: String): AppRefreshToken?
    fun removeByTokenId(tokenId: String)
    fun deleteById(id: java.lang.Long)
    fun save(token: AppRefreshToken): AppRefreshToken
}