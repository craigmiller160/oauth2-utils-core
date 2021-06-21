package io.craigmiller160.oauth2.client

import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.dto.TokenResponseDto
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class AuthServerClientImplTest {

    private val host = "host"
    private val path = "path"
    private val key = "key"
    private val secret = "secret"
    private val redirectUri = "redirectUri"
    private val authHeader = "Basic a2V5OnNlY3JldA=="
    private val response = TokenResponseDto("access", "refresh", "id")

    @MockK
    private lateinit var oAuthConfig: OAuth2Config

    private lateinit var authServerClient: AuthServerClientImpl
    private var request: AuthServerClientRequest? = null

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        every { oAuthConfig.authServerHost } returns host
        every { oAuthConfig.clientKey } returns key
        every { oAuthConfig.clientSecret } returns secret

        authServerClient = AuthServerClientImpl(oAuthConfig, this::handleRequest)
        request = null
    }

    private fun handleRequest(request: AuthServerClientRequest): TokenResponseDto {
        this.request = request
        return response
    }

    @Test
    fun test_authenticateAuthCode() {
        every { oAuthConfig.authCodeRedirectUri } returns redirectUri

        val authCode = "DERFG"
//        val entityCaptor = ArgumentCaptor.forClass(HttpEntity::class.java)
//
//        `when`(restTemplate.exchange(
//                eq("$host${OAuth2Config.TOKEN_PATH}"),
//                eq(HttpMethod.POST),
//                entityCaptor.capture(),
//                eq(TokenResponseDto::class.java)
//        ))
//                .thenReturn(ResponseEntity.ok(response))
//
        val result = authServerClient.authenticateAuthCode(host, authCode)
        assertEquals(response, result)

        assertNotNull(request)
        assertEquals(key, request?.clientKey)
        assertEquals(secret, request?.clientSecret)
//
//        Assertions.assertEquals(1, entityCaptor.allValues.size)
//        val entity = entityCaptor.value
//
//        Assertions.assertEquals(this.authHeader, entity.headers["Authorization"]?.get(0))
//        assertEquals(MediaType.APPLICATION_FORM_URLENCODED_VALUE, entity.headers["Content-Type"]?.get(0))
//
//        val body = entity.body
//        Assertions.assertTrue(body is MultiValueMap<*, *>)
//        val map = body as MultiValueMap<String, String>
//        Assertions.assertEquals("authorization_code", map["grant_type"]?.get(0))
//        Assertions.assertEquals("$host$redirectUri", map["redirect_uri"]?.get(0))
//        Assertions.assertEquals(key, map["client_id"]?.get(0))
//        Assertions.assertEquals(authCode, map["code"]?.get(0))
        TODO("Finish this")
    }

    @Test
    fun test_authenticateRefreshToken_invalidResponseBody() {
        val refreshToken = "ABCDEFG"

//        `when`(restTemplate.exchange(
//                eq("$host${OAuth2Config.TOKEN_PATH}"),
//                eq(HttpMethod.POST),
//                isA(HttpEntity::class.java),
//                eq(TokenResponseDto::class.java)
//        ))
//                .thenReturn(ResponseEntity.noContent().build())
//
//        assertThrows<InvalidResponseBodyException> { authServerClient.authenticateRefreshToken(refreshToken) }
        TODO("Finish this")
    }

    @Test
    fun test_authenticateRefreshToken() {
        val refreshToken = "ABCDEFG"

//        val entityCaptor = ArgumentCaptor.forClass(HttpEntity::class.java)
//
//        `when`(restTemplate.exchange(
//                eq("$host${OAuth2Config.TOKEN_PATH}"),
//                eq(HttpMethod.POST),
//                entityCaptor.capture(),
//                eq(TokenResponseDto::class.java)
//        ))
//                .thenReturn(ResponseEntity.ok(response))
//
//        val result = authServerClient.authenticateRefreshToken(refreshToken)
//        Assertions.assertEquals(response, result)
//
//        Assertions.assertEquals(1, entityCaptor.allValues.size)
//        val entity = entityCaptor.value
//
//        Assertions.assertEquals(this.authHeader, entity.headers["Authorization"]?.get(0))
//        assertEquals(MediaType.APPLICATION_FORM_URLENCODED_VALUE, entity.headers["Content-Type"]?.get(0))
//
//        val body = entity.body
//        Assertions.assertTrue(body is MultiValueMap<*, *>)
//        val map = body as MultiValueMap<String, String>
//        Assertions.assertEquals("refresh_token", map["grant_type"]?.get(0))
//        Assertions.assertEquals(refreshToken, map["refresh_token"]?.get(0))
        TODO("Finish this")
    }

}