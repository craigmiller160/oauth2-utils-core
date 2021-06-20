package io.craigmiller160.oauth2.client

import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.dto.TokenResponseDto
import io.craigmiller160.oauth2.exception.BadAuthenticationException
import io.craigmiller160.oauth2.exception.InvalidResponseBodyException

// TODO migrate tests
class AuthServerClientImpl(
        private val oAuth2Config: OAuth2Config,
        private val executeRequest: (AuthServerClientRequest) -> TokenResponseDto?
) : AuthServerClient {
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

        val request = AuthServerClientRequest(
                url = url,
                clientKey = oAuth2Config.clientKey,
                clientSecret = oAuth2Config.clientSecret,
                body = body
        )

        try {
            return executeRequest(request) ?: throw InvalidResponseBodyException()
        } catch (ex: Exception) {
            when (ex) {
                is InvalidResponseBodyException -> throw ex
                else -> throw BadAuthenticationException("Error while requesting authentication token", ex)
            }
        }
    }
}