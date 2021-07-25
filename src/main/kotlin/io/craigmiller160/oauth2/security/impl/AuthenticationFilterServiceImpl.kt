package io.craigmiller160.oauth2.security.impl

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.BadJOSEException
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.exception.InvalidTokenException
import io.craigmiller160.oauth2.security.AuthenticationFilterService
import io.craigmiller160.oauth2.security.RequestWrapper
import io.craigmiller160.oauth2.service.RefreshTokenService
import java.lang.RuntimeException
import java.text.ParseException

class AuthenticationFilterServiceImpl(
        private val oAuth2Config: OAuth2Config,
        private val refreshTokenService: RefreshTokenService
) : AuthenticationFilterService {
    override fun authenticateRequest(req: RequestWrapper) {
        runCatching {
            val token = getToken(req)
        }
    }

    private fun validateToken(token: String, req: RequestWrapper, alreadyAttemptedRefresh: Boolean = false): Result<JWTClaimsSet> {
        val jwtProcessor = DefaultJWTProcessor<SecurityContext>()
        val keySource = ImmutableJWKSet<SecurityContext>(oAuth2Config.jwkSet)
        val expectedAlg = JWSAlgorithm.RS256
        val keySelector = JWSVerificationKeySelector(expectedAlg, keySource)
        jwtProcessor.jwsKeySelector = keySelector

        val claimsVerifier = DefaultJWTClaimsVerifier<SecurityContext>(
                JWTClaimsSet.Builder()
                        .claim("clientKey", oAuth2Config.clientKey)
                        .claim("clientName", oAuth2Config.clientName)
                        .build(),
                setOf("sub", "exp", "iat", "jti")
        )
        jwtProcessor.jwtClaimsSetVerifier = claimsVerifier

        return runCatching {
            jwtProcessor.process(token, null)
        }.recoverCatching { ex ->
            when(ex) {
                is BadJOSEException -> {
                    if (alreadyAttemptedRefresh) {
                        throw InvalidTokenException("Token validation failed: ${ex.message}", ex)
                    }
                    attemptRefresh(token, req).getOrThrow()
                }
                is ParseException, is JOSEException ->
                    throw InvalidTokenException("Token validation failed: ${ex.message}", ex)
                is RuntimeException -> throw ex
                else -> throw RuntimeException(ex)
            }
        }
    }

    private fun attemptRefresh(token: String, req: RequestWrapper): Result<JWTClaimsSet> = runCatching {
        refreshTokenService.refreshToken(token)
                ?.let { tokenResponse ->
                    // TODO need to set cookie
                    validateToken(tokenResponse.accessToken, req, true).getOrThrow()
                }
                ?: throw InvalidTokenException("Token refresh failed")
    }

    private fun getToken(req: RequestWrapper): String {
        return req.getBearerToken()
                ?: req.getCookieToken(oAuth2Config.cookieName)
                ?: throw InvalidTokenException("Token not found")
    }
}