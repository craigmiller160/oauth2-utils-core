package io.craigmiller160.oauth2.service.impl

import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.domain.entity.AppRefreshToken
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import io.craigmiller160.oauth2.dto.AuthCodeSuccessDto
import io.craigmiller160.oauth2.exception.BadAuthCodeRequestException
import io.craigmiller160.oauth2.exception.BadAuthCodeStateException
import io.craigmiller160.oauth2.security.CookieCreator
import io.craigmiller160.oauth2.service.AuthCodeService
import java.math.BigInteger
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.servlet.http.HttpServletRequest

class AuthCodeServiceImpl(
        private val oAuth2Config: OAuth2Config,
        private val authServerClient: AuthServerClient,
        private val appRefreshTokenRepo: AppRefreshTokenRepository,
        private val cookieCreator: CookieCreator
) : AuthCodeService {

    private fun generateAuthCodeState(): String {
        val random = SecureRandom()
        val bigInt = BigInteger(130, random)
        return bigInt.toString(32)
    }

    override fun prepareAuthCodeLogin(req: HttpServletRequest): String {
        val origin = req.getHeader("Origin")
                ?: throw BadAuthCodeRequestException("Missing origin header on request")

        val state = generateAuthCodeState()
        req.session.setAttribute(AuthCodeService.STATE_ATTR, state)
        req.session.setAttribute(AuthCodeService.STATE_EXP_ATTR, ZonedDateTime.now(ZoneId.of("UTC")).plusMinutes(oAuth2Config.authCodeWaitMins))
        req.session.setAttribute(AuthCodeService.ORIGIN, origin)

        val loginPath = OAuth2Config.AUTH_CODE_LOGIN_PATH
        val clientKey = URLEncoder.encode(oAuth2Config.clientKey, StandardCharsets.UTF_8)
        val encodedState = URLEncoder.encode(state, StandardCharsets.UTF_8)

        val redirectUri = URLEncoder.encode("$origin${oAuth2Config.authCodeRedirectUri}", StandardCharsets.UTF_8)
        val host = "$origin${oAuth2Config.authLoginBaseUri}"

        return "$host$loginPath?response_type=code&client_id=$clientKey&redirect_uri=$redirectUri&state=$encodedState"
    }

    override fun code(req: HttpServletRequest, code: String, state: String): AuthCodeSuccessDto {
        val expectedState = req.session.getAttribute(AuthCodeService.STATE_ATTR) as String?
        val stateExp = req.session.getAttribute(AuthCodeService.STATE_EXP_ATTR) as ZonedDateTime?
        if (expectedState != state) {
            throw BadAuthCodeStateException("State does not match expected value")
        }
        if (stateExp == null || ZonedDateTime.now(ZoneId.of("UTC")) > stateExp) {
            throw BadAuthCodeStateException("Auth code state has expired")
        }

        val origin = req.session.getAttribute(AuthCodeService.ORIGIN) as String?
                ?: throw BadAuthCodeRequestException("Missing origin attribute in session")

        req.session.removeAttribute(AuthCodeService.STATE_ATTR)
        req.session.removeAttribute(AuthCodeService.STATE_EXP_ATTR)
        req.session.removeAttribute(AuthCodeService.ORIGIN)

        val tokens = authServerClient.authenticateAuthCode(origin, code)
        val manageRefreshToken = AppRefreshToken(0, tokens.tokenId, tokens.refreshToken)
        appRefreshTokenRepo.removeByTokenId(tokens.tokenId)
        appRefreshTokenRepo.save(manageRefreshToken)
        val cookie = cookieCreator.createTokenCookie(oAuth2Config.cookieName, oAuth2Config.getOrDefaultCookiePath(), tokens.accessToken, oAuth2Config.cookieMaxAgeSecs)
        return AuthCodeSuccessDto(cookie, oAuth2Config.postAuthRedirect)
    }
}