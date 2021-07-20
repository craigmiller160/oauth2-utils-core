package io.craigmiller160.oauth2.service.impl

import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import io.craigmiller160.oauth2.security.CookieCreator
import io.craigmiller160.oauth2.service.AuthCodeService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
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
    fun test() {
        TODO("Finish this")
    }

}