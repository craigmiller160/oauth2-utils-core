package io.craigmiller160.oauth2.security.impl

import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.security.AuthenticationFilterService
import io.craigmiller160.oauth2.security.RequestWrapper
import io.craigmiller160.oauth2.service.RefreshTokenService
import io.craigmiller160.oauth2.testutils.JwtUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import java.security.KeyPair

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationFilterServiceImplTest {

    private lateinit var oAuthConfig: OAuth2Config
    private lateinit var jwkSet: JWKSet
    private lateinit var keyPair: KeyPair
    private lateinit var token: String
    private val cookieName = "cookie"

    @Mock
    private lateinit var refreshTokenService: RefreshTokenService
    @Mock
    private lateinit var req: RequestWrapper
    @Mock
    private lateinit var cookieConfig: OAuth2Config

    @InjectMocks
    private lateinit var authFilterService: AuthenticationFilterService

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
        `when`(oAuthConfig.insecurePaths)
                .thenReturn("/other/path")
        `when`(oAuthConfig.jwkSet)
                .thenReturn(jwkSet)

        val jwt = JwtUtils.createJwt()
        token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
    }

    @Test
    fun `authenticate with valid bearer token`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate with default insecure path`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate with configured insecure path`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate without token`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate with valid cookie token`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate with authcode URI`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate with bad signature`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate with wrong client`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate with expired token`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate with token without bearer prefix`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate with successful refresh`() {
        TODO("Finish this")
    }

    @Test
    fun `authenticate with failed refresh`() {
        TODO("Finish this")
    }



}