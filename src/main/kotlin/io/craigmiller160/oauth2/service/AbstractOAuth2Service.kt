package io.craigmiller160.oauth2.service

import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import io.craigmiller160.oauth2.dto.AuthUserDto
import io.craigmiller160.oauth2.security.AuthenticatedUser
import io.craigmiller160.oauth2.security.CookieCreator

abstract class AbstractOAuth2Service(
        private val oAuth2Config: OAuth2Config,
        private val appRefreshTokenRepo: AppRefreshTokenRepository
) : OAuth2Service {

    protected abstract fun getAuthUserContext(): AuthenticatedUser

    override fun logout(): String {
        val authUser = getAuthUserContext()
        appRefreshTokenRepo.removeByTokenId(authUser.tokenId)
        return CookieCreator.createTokenCookie(oAuth2Config.cookieName, oAuth2Config.getOrDefaultCookiePath(), "", 0)
    }

    override fun getAuthenticatedUser(): AuthUserDto {
        val authUser = getAuthUserContext()
        TODO("Finish this")
    }

}