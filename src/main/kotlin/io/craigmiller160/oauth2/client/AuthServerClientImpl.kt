package io.craigmiller160.oauth2.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.dto.TokenResponseDto
import io.craigmiller160.oauth2.exception.BadAuthenticationException
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.Flow
import javax.net.ssl.SSLContext

typealias HttpClientProvider = () -> HttpClient
typealias BodyPublisherProvider = (String) -> HttpRequest.BodyPublisher
val defaultBodyPublisherProvider: BodyPublisherProvider = { value -> HttpRequest.BodyPublishers.ofString(value) }
val defaultHttpClientProvider: HttpClientProvider = {
    HttpClient.newBuilder()
            .sslContext(SSLContext.getInstance("TLS"))
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build()
}

class AuthServerClientImpl(
        private val oAuth2Config: OAuth2Config,
        private val clientProvider: HttpClientProvider,
        private val bodyPublisherProvider: BodyPublisherProvider
) : AuthServerClient {

    constructor(oAuth2Config: OAuth2Config):
            this(oAuth2Config, defaultHttpClientProvider, defaultBodyPublisherProvider)

    private val client: HttpClient = clientProvider()
    private val objectMapper = ObjectMapper().registerKotlinModule()

    override fun authenticateAuthCode(origin: String, code: String): TokenResponseDto {
        val clientKey = oAuth2Config.clientKey
        val redirectUri = "$origin${oAuth2Config.authCodeRedirectUri}"

        val body = mapOf(
                "grant_type" to "authorization_code",
                "client_id" to clientKey,
                "code" to code,
                "redirect_uri" to redirectUri
        )

        return tokenRequest(body)
    }

    override fun authenticateRefreshToken(refreshToken: String): TokenResponseDto {
        val body = mapOf(
                "grant_type" to "refresh_token",
                "refresh_token" to refreshToken
        )
        return tokenRequest(body)
    }

    private fun tokenRequest(body: Map<String,String>): TokenResponseDto {
        val host = oAuth2Config.authServerHost
        val path = OAuth2Config.TOKEN_PATH

        val url = "$host$path"

        val auth = Base64.getEncoder().encodeToString("${oAuth2Config.clientKey}:${oAuth2Config.clientSecret}".toByteArray())
        val bodyString = body
                .entries
                .joinToString(separator = "&") { entry ->
                    val key = URLEncoder.encode(entry.key, StandardCharsets.UTF_8)
                    val value = URLEncoder.encode(entry.value, StandardCharsets.UTF_8)
                    "$key=$value"
                }

        return runCatching {
            val request = HttpRequest.newBuilder(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic $auth")
                    .POST(bodyPublisherProvider(bodyString))
                    .build()

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply { it.body() }
                    .thenApply { objectMapper.readValue(it, TokenResponseDto::class.java) }
                    .get()
        }
                .recoverCatching { ex ->
                    throw BadAuthenticationException("Error while requesting authentication token", ex)
                }
                .getOrThrow()
    }

    class ClientBodyPublisher(val content: String) : HttpRequest.BodyPublisher {
        override fun subscribe(subscriber: Flow.Subscriber<in ByteBuffer>?) {}

        override fun contentLength(): Long {
            return content.length.toLong()
        }

    }
}