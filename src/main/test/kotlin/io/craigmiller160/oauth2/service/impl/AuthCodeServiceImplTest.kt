package io.craigmiller160.oauth2.service.impl

import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.domain.entity.AppRefreshToken
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import io.craigmiller160.oauth2.dto.TokenResponseDto
import io.craigmiller160.oauth2.exception.BadAuthCodeRequestException
import io.craigmiller160.oauth2.exception.BadAuthCodeStateException
import io.craigmiller160.oauth2.security.CookieCreator
import io.craigmiller160.oauth2.service.AuthCodeService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@ExtendWith(MockitoExtension::class)
class AuthCodeServiceImplTest {

    private val host = "host"
    private val path = "path"
    private val redirectUri = "redirectUri"
    private val clientKey = "clientKey"
    private val cookieExpSecs = 30L
    private val cookieName = "cookie"
    private val postAuthRedirect = "postAuthRedirect"
    private val origin = "TheOrigin"

    @Mock
    private lateinit var oAuthConfig: OAuth2Config

    @Mock
    private lateinit var authServerClient: AuthServerClient

    @Mock
    private lateinit var appRefreshTokenRepo: AppRefreshTokenRepository

    @Mock
    private lateinit var req: HttpServletRequest

    @Mock
    private lateinit var session: HttpSession

    @Mock
    private lateinit var cookieCreator: CookieCreator

    @InjectMocks
    private lateinit var authCodeService: AuthCodeServiceImpl

    @Test
    fun test_prepareAuthCodeLogin() {
        Mockito.`when`(req.session)
                .thenReturn(session)
        Mockito.`when`(req.getHeader("Origin"))
                .thenReturn(origin)
        Mockito.`when`(oAuthConfig.authCodeRedirectUri)
                .thenReturn(redirectUri)
        Mockito.`when`(oAuthConfig.clientKey)
                .thenReturn(clientKey)
        Mockito.`when`(oAuthConfig.authLoginBaseUri)
                .thenReturn("")

        val result = authCodeService.prepareAuthCodeLogin(req)

        val captor = ArgumentCaptor.forClass(String::class.java)

        Mockito.verify(session, Mockito.times(1))
                .setAttribute(eq(AuthCodeService.STATE_ATTR), captor.capture())
        Mockito.verify(session, Mockito.times(1))
                .setAttribute(eq(AuthCodeService.STATE_EXP_ATTR), any())
        Mockito.verify(session, Mockito.times(1))
                .setAttribute(AuthCodeService.ORIGIN, origin)

        Assertions.assertNotNull(captor.value)
        val state = captor.value

        val expected = "$origin${OAuth2Config.AUTH_CODE_LOGIN_PATH}?response_type=code&client_id=$clientKey&redirect_uri=$origin$redirectUri&state=$state"
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun test_code() {
        Mockito.`when`(req.session)
                .thenReturn(session)
        Mockito.`when`(oAuthConfig.cookieMaxAgeSecs)
                .thenReturn(cookieExpSecs)
        Mockito.`when`(oAuthConfig.cookieName)
                .thenReturn(cookieName)
        Mockito.`when`(oAuthConfig.postAuthRedirect)
                .thenReturn(postAuthRedirect)
        Mockito.`when`(oAuthConfig.getOrDefaultCookiePath())
                .thenReturn(path)

        val authCode = "DEF"
        val state = "ABC"
        Mockito.`when`(session.getAttribute(AuthCodeService.STATE_ATTR))
                .thenReturn(state)
        Mockito.`when`(session.getAttribute(AuthCodeService.STATE_EXP_ATTR))
                .thenReturn(ZonedDateTime.now(ZoneId.of("UTC")).plusDays(1))
        Mockito.`when`(session.getAttribute(AuthCodeService.ORIGIN))
                .thenReturn(origin)

        val response = TokenResponseDto("access", "refresh", "id")
        Mockito.`when`(authServerClient.authenticateAuthCode(origin, authCode))
                .thenReturn(response)

        Mockito.`when`(cookieCreator.createTokenCookie(cookieName, oAuthConfig.getOrDefaultCookiePath(), "access", oAuthConfig.cookieMaxAgeSecs))
                .thenReturn("Cookie")

        val (cookie, redirect) = authCodeService.code(req, authCode, state)
        Assertions.assertEquals(postAuthRedirect, redirect)
        Assertions.assertEquals("Cookie", cookie)

        val manageRefreshToken = AppRefreshToken(0, response.tokenId, response.refreshToken)
        Mockito.verify(appRefreshTokenRepo, Mockito.times(1))
                .save(manageRefreshToken)

        Mockito.verify(session, Mockito.times(1))
                .removeAttribute(AuthCodeService.STATE_ATTR)
        Mockito.verify(session, Mockito.times(1))
                .removeAttribute(AuthCodeService.STATE_EXP_ATTR)
        Mockito.verify(session, Mockito.times(1))
                .removeAttribute(AuthCodeService.ORIGIN)
        Mockito.verify(appRefreshTokenRepo, Mockito.times(1))
                .removeByTokenId(response.tokenId)
    }

    @Test
    fun test_code_badState() {
        Mockito.`when`(req.session)
                .thenReturn(session)
        val authCode = "DEF"
        val state = "ABC"

        val ex = assertThrows<BadAuthCodeStateException> { authCodeService.code(req, authCode, state) }
        Assertions.assertEquals("State does not match expected value", ex.message)
    }

    @Test
    fun test_code_stateExp() {
        Mockito.`when`(req.session)
                .thenReturn(session)
        val authCode = "DEF"
        val state = "ABC"

        Mockito.`when`(session.getAttribute(AuthCodeService.STATE_ATTR))
                .thenReturn(state)
        Mockito.`when`(session.getAttribute(AuthCodeService.STATE_EXP_ATTR))
                .thenReturn(ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1))

        val ex = assertThrows<BadAuthCodeStateException> { authCodeService.code(req, authCode, state) }
        Assertions.assertEquals("Auth code state has expired", ex.message)
    }

    @Test
    fun test_code_noOrigin() {
        Mockito.`when`(req.session)
                .thenReturn(session)
        val authCode = "DEF"
        val state = "ABC"

        Mockito.`when`(session.getAttribute(AuthCodeService.STATE_ATTR))
                .thenReturn(state)
        Mockito.`when`(session.getAttribute(AuthCodeService.STATE_EXP_ATTR))
                .thenReturn(ZonedDateTime.now(ZoneId.of("UTC")).plusDays(1))

        val ex = assertThrows<BadAuthCodeRequestException> { authCodeService.code(req, authCode, state) }
        Assertions.assertEquals("Missing origin attribute in session", ex.message)
    }

}