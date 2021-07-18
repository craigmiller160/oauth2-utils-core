package io.craigmiller160.oauth2.service.impl

import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import io.craigmiller160.oauth2.dto.AuthUserDto
import io.craigmiller160.oauth2.dto.authenticatedUserToAuthUserDto
import io.craigmiller160.oauth2.security.CookieCreator
import io.craigmiller160.oauth2.service.AuthUserProvider
import io.craigmiller160.oauth2.service.OAuth2Service

class OAuth2ServiceImpl(
        private val oAuth2Config: OAuth2Config,
        private val appRefreshTokenRepo: AppRefreshTokenRepository,
        private val cookieCreator: CookieCreator,
        private val authUserProvider: AuthUserProvider
) : OAuth2Service {

    override fun logout(): String {
        val authUser = authUserProvider.provide()
        appRefreshTokenRepo.removeByTokenId(authUser.tokenId)
        return cookieCreator.createTokenCookie(oAuth2Config.cookieName, oAuth2Config.getOrDefaultCookiePath(), "", 0)
    }

    override fun getAuthenticatedUser(): AuthUserDto {
        val authUser = authUserProvider.provide()
        return authenticatedUserToAuthUserDto(authUser)
    }

}