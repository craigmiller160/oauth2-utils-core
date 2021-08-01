package io.craigmiller160.oauth2.security.impl

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jwt.JWTClaimsSet
import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.dto.TokenResponseDto
import io.craigmiller160.oauth2.exception.InvalidTokenException
import io.craigmiller160.oauth2.security.CookieCreator
import io.craigmiller160.oauth2.security.RequestWrapper
import io.craigmiller160.oauth2.service.RefreshTokenService
import io.craigmiller160.oauth2.testutils.JwtUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import java.security.KeyPair
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationFilterServiceImplTest {

    private lateinit var jwkSet: JWKSet
    private lateinit var keyPair: KeyPair
    private lateinit var token: String
    private val cookieName = "cookie"

    @Mock
    private lateinit var refreshTokenService: RefreshTokenService
    @Mock
    private lateinit var cookieCreator: CookieCreator
    @Mock
    private lateinit var oAuthConfig: OAuth2Config

    @InjectMocks
    private lateinit var authFilterService: AuthenticationFilterServiceImpl

    @BeforeEach
    fun setup() {
        keyPair = JwtUtils.createKeyPair()
        jwkSet = JwtUtils.createJwkSet(keyPair)
        `when`(oAuthConfig.clientKey)
                .thenReturn(JwtUtils.CLIENT_KEY)
        `when`(oAuthConfig.clientName)
                .thenReturn(JwtUtils.CLIENT_NAME)
        `when`(oAuthConfig.cookieName)
                .thenReturn(cookieName)
        `when`(oAuthConfig.getInsecurePathList())
                .thenReturn(listOf("/other/path"))
        `when`(oAuthConfig.jwkSet)
                .thenReturn(jwkSet)

        val jwt = JwtUtils.createJwt()
        token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
    }

    private fun getTokenHeader(headerName: String): String? =
            when (headerName) {
                "Authorization" -> "Bearer $token"
                else -> null
            }

    @Test
    fun `authenticate with valid bearer token`() {
        var claims: JWTClaimsSet? = null
        var cookie: String? = null
        val req = RequestWrapper(
                requestUri = "/something",
                getCookieValue = {null},
                getHeaderValue = this::getTokenHeader,
                setAuthentication = { claims = it },
                setNewTokenCookie = { cookie = it }
        )
        authFilterService.authenticateRequest(req).getOrThrow()

        assertNotNull(claims)
        assertNull(cookie)

        assertEquals(JwtUtils.USERNAME, claims?.subject)
        assertEquals(listOf(JwtUtils.ROLE_1, JwtUtils.ROLE_2), claims?.getStringListClaim("roles"))
        verify(refreshTokenService, times(0))
                .refreshToken(any())
    }

    @Test
    fun `authenticate with default insecure path`() {
//        `when`(req.requestUri)
//                .thenReturn("/oauth/authcode/login")
//        authFilterService.authenticateRequest(req)
//
//        verify(req, times(0))
//                .setAuthentication(any())
//        verify(refreshTokenService, times(0))
//                .refreshToken(any())
        TODO("Finish this")
    }

    @Test
    fun `authenticate with configured insecure path`() {
//        `when`(req.requestUri)
//                .thenReturn("/other/path")
//        authFilterService.authenticateRequest(req)
//
//        verify(req, times(0))
//                .setAuthentication(any())
//        verify(refreshTokenService, times(0))
//                .refreshToken(any())
        TODO("Finish this")
    }

    @Test
    fun `authenticate without token`() {
//        `when`(req.requestUri)
//                .thenReturn("/something")
//        assertFailsWith<InvalidTokenException>(message = "Token not found") {
//            authFilterService.authenticateRequest(req).getOrThrow()
//        }
        TODO("Finish this")
    }

    @Test
    fun `authenticate with valid cookie token`() {
//        `when`(req.requestUri)
//                .thenReturn("/something")
//        `when`(req.getCookieValue(cookieName))
//                .thenReturn(token)
//
//        authFilterService.authenticateRequest(req)
//
//        val captor = argumentCaptor<JWTClaimsSet>()
//        verify(req, times(1))
//                .setAuthentication(captor.capture())
//        assertEquals(JwtUtils.USERNAME, captor.firstValue.subject)
//        assertEquals(listOf(JwtUtils.ROLE_1, JwtUtils.ROLE_2), captor.firstValue.getStringListClaim("roles"))
//        verify(refreshTokenService, times(0))
//                .refreshToken(any())
        TODO("Finish this")
    }

    @Test
    fun `authenticate with bad signature`() {
//        `when`(req.requestUri)
//                .thenReturn("/something")
//        val keyPair = JwtUtils.createKeyPair()
//        val jwt = JwtUtils.createJwt()
//        val token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
//        `when`(req.getHeaderValue("Authorization"))
//                .thenReturn("Bearer $token")
//
//        val ex = assertFailsWith<InvalidTokenException> {
//            authFilterService.authenticateRequest(req).getOrThrow()
//        }
//        assertTrue {
//            ex.message?.contains("Invalid signature") ?: false
//        }
        TODO("Finish this")
    }

    @Test
    fun `authenticate with wrong client`() {
//        `when`(req.requestUri)
//                .thenReturn("/something")
//        `when`(oAuthConfig.clientKey)
//                .thenReturn("ABCDEFG")
//        `when`(req.getHeaderValue("Authorization"))
//                .thenReturn("Bearer $token")
//
//        val ex = assertFailsWith<InvalidTokenException> {
//            authFilterService.authenticateRequest(req).getOrThrow()
//        }
//        assertTrue { ex.message?.contains("""JWT "clientKey" claim has value clientKey, must be ABCDEFG""") ?: false }
        TODO("Finish this")
    }

    @Test
    fun `authenticate with expired token and refresh failed`() {
//        `when`(req.requestUri)
//                .thenReturn("/something")
//        val jwt = JwtUtils.createJwt(-20)
//        val token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
//        `when`(req.getHeaderValue("Authorization"))
//                .thenReturn("Bearer $token")
//
//        assertFailsWith<InvalidTokenException>(message = "Token refresh failed") {
//            authFilterService.authenticateRequest(req).getOrThrow()
//        }
        TODO("Finish this")
    }

    @Test
    fun `authenticate with token without bearer prefix`() {
//        `when`(req.requestUri)
//                .thenReturn("/something")
//        `when`(req.getHeaderValue("Authorization"))
//                .thenReturn(token)
//        val ex = assertFailsWith<InvalidTokenException> {
//            authFilterService.authenticateRequest(req).getOrThrow()
//        }
//        assertEquals("Not bearer token", ex.message)
        TODO("Finish this")
    }

    @Test
    fun `authenticate with successful refresh`() {
//        `when`(req.requestUri)
//                .thenReturn("/something")
//        val jwt = JwtUtils.createJwt(-20)
//        val token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
//        `when`(req.getHeaderValue("Authorization"))
//                .thenReturn("Bearer $token")
//        val refreshToken = "ABCDEFG"
//        val newRefreshToken = "HIJKLMNO"
//        val newTokenId = "id2"
//
//        `when`(refreshTokenService.refreshToken(token))
//                .thenReturn(TokenResponseDto(this.token, newRefreshToken, newTokenId))
//
//        `when`(cookieCreator.createTokenCookie(cookieName, oAuthConfig.getOrDefaultCookiePath(), this.token, oAuthConfig.cookieMaxAgeSecs))
//                .thenReturn("Cookie")
//
//        authFilterService.authenticateRequest(req)
//
//        val captor = argumentCaptor<JWTClaimsSet>()
//        verify(req, times(1))
//                .setAuthentication(captor.capture())
//        assertEquals(JwtUtils.USERNAME, captor.firstValue.subject)
//        assertEquals(listOf(JwtUtils.ROLE_1, JwtUtils.ROLE_2), captor.firstValue.getStringListClaim("roles"))
//
//        verify(refreshTokenService, times(1))
//                .refreshToken(token)
//        verify(req, times(1))
//                .setNewTokenCookie("Cookie")
        TODO("Finish this")
    }

}