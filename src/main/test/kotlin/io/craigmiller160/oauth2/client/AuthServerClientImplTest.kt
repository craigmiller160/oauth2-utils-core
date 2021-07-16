package io.craigmiller160.oauth2.client

import com.fasterxml.jackson.databind.ObjectMapper
import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.dto.TokenResponseDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.junit.jupiter.MockitoExtension
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class AuthServerClientImplTest {

    private val accessToken = "accessToken"
    private val refreshToken = "refreshToken"
    private val tokenId = "tokenId"
    private val host = "http://localhost"
    private val path = "path"
    private val key = "key"
    private val secret = "secret"
    private val redirectUri = "redirectUri"
    private val authHeader = "Basic a2V5OnNlY3JldA=="

    @Mock
    private lateinit var oAuthConfig: OAuth2Config
    @Mock
    private lateinit var client: HttpClient
    @Mock
    private lateinit var response: HttpResponse<String>
    @Mock
    private lateinit var bodyPublisher: HttpRequest.BodyPublisher

    private var bodyValue: String = ""
    private lateinit var tokenResponse: TokenResponseDto
    private lateinit var authServerClient: AuthServerClientImpl
    private val mapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        `when`(oAuthConfig.authServerHost)
                .thenReturn(host)
        `when`(oAuthConfig.clientKey)
                .thenReturn(key)
        `when`(oAuthConfig.clientSecret)
                .thenReturn(secret)

        authServerClient = AuthServerClientImpl(oAuthConfig, client) { value ->
            bodyValue = value
            bodyPublisher
        }
        tokenResponse = TokenResponseDto(
                accessToken = accessToken,
                refreshToken = refreshToken,
                tokenId = tokenId
        )
        `when`(response.body())
                .thenReturn(mapper.writeValueAsString(tokenResponse))
        bodyValue = ""
    }

    @Test
    fun test_authenticateAuthCode() {
        `when`(oAuthConfig.authCodeRedirectUri)
                .thenReturn(redirectUri)

        val authCode = "DERFG"
        val requestCaptor = ArgumentCaptor.forClass(HttpRequest::class.java)
        `when`(client.sendAsync(requestCaptor.capture(), any<HttpResponse.BodyHandler<String>>()))
                .thenReturn(CompletableFuture.completedFuture(response))

        val result = authServerClient.authenticateAuthCode(host, authCode)
        assertEquals(tokenResponse, result)

        val request = requestCaptor.value
        assertNotNull(request)
        assertEquals("application/x-www-form-urlencoded", request.headers().firstValue("Content-Type").get())
        assertEquals(authHeader, request.headers().firstValue("Authorization").get())
        assertEquals("grant_type=authorization_code&client_id=key&code=DERFG&redirect_uri=http%3A%2F%2FlocalhostredirectUri", bodyValue)
    }

    @Test
    fun test_authenticateRefreshToken_authError() {
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