package io.craigmiller160.oauth2.service.impl

import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import io.craigmiller160.oauth2.dto.AuthCodeSuccessDto
import io.craigmiller160.oauth2.security.CookieCreator
import io.craigmiller160.oauth2.service.AuthCodeService
import javax.servlet.http.HttpServletRequest

class AuthCodeServiceImpl(
        private val oAuthConfig: OAuth2Config,
        private val authServerClient: AuthServerClient,
        private val appRefreshTokenRepo: AppRefreshTokenRepository,
        private val cookieCreator: CookieCreator
) : AuthCodeService {
    override fun prepareAuthCodeLogin(req: HttpServletRequest): String {
        TODO("Not yet implemented")
    }

    override fun code(req: HttpServletRequest, code: String, state: String): AuthCodeSuccessDto {
        TODO("Not yet implemented")
    }
}