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
import io.craigmiller160.kotlin.result.flatMap
import io.craigmiller160.kotlin.result.flatRecoverCatching
import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.exception.InvalidTokenException
import io.craigmiller160.oauth2.security.AuthenticationFilterService
import io.craigmiller160.oauth2.security.CookieCreator
import io.craigmiller160.oauth2.security.RequestWrapper
import io.craigmiller160.oauth2.service.RefreshTokenService
import org.apache.shiro.util.AntPathMatcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.RuntimeException
import java.text.ParseException

class AuthenticationFilterServiceImpl(
        private val oAuth2Config: OAuth2Config,
        private val refreshTokenService: RefreshTokenService,
        private val cookieCreator: CookieCreator
) : AuthenticationFilterService {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private val DEFAULT_INSECURE_URI_PATTERNS = listOf("/oauth/authcode/**", "logout")
    }

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun authenticateRequest(req: RequestWrapper): Result<Unit> {
        return if (isUriSecured(req.getRequestUri())) {
            logger.debug("Authenticating request")
            runCatching { getToken(req) }
                    .flatMap { token -> validateToken(token, req) }
                    .map { claims -> req.setAuthentication(claims) }
                    .onFailure { ex -> logger.error("Token validation failed", ex) }
        } else {
            logger.debug("Skipping authentication for insecure URI: ${req.getRequestUri()}")
            return Result.success(Unit)
        }
    }

    private fun isUriSecured(requestUri: String): Boolean {
        val antMatcher = AntPathMatcher()
        return !DEFAULT_INSECURE_URI_PATTERNS.any { antMatcher.match(it, requestUri) }
                && !oAuth2Config.getInsecurePathList().any { antMatcher.match(it, requestUri) }
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
        }.flatRecoverCatching { ex ->
            when {
                ex is BadJOSEException && ex.message == "Expired JWT" -> {
                    if (alreadyAttemptedRefresh) {
                        throw InvalidTokenException("Token validation failed: ${ex.message}", ex)
                    }
                    attemptRefresh(token, req)
                }
                ex is BadJOSEException || ex is ParseException || ex is JOSEException ->
                    throw InvalidTokenException("Token validation failed: ${ex.message}", ex)
                ex is RuntimeException -> throw ex
                else -> throw RuntimeException(ex)
            }
        }
    }

    private fun attemptRefresh(token: String, req: RequestWrapper): Result<JWTClaimsSet> {
        return runCatching {
            refreshTokenService.refreshToken(token)
                    ?: throw InvalidTokenException("Token refresh failed")
        }
                .flatMap { tokenResponse ->
                    validateToken(tokenResponse.accessToken, req, true)
                            .map { claims -> Pair(claims, tokenResponse.accessToken) }
                }
                .map { (claims, accessToken) ->
                    val cookie = cookieCreator.createTokenCookie(oAuth2Config.cookieName, oAuth2Config.getOrDefaultCookiePath(), accessToken, oAuth2Config.cookieMaxAgeSecs)
                    req.setNewTokenCookie(cookie)
                    claims
                }
    }

    private fun getToken(req: RequestWrapper): String {
        return getBearerToken(req)
                ?: req.getCookieValue(oAuth2Config.cookieName)
                ?: throw InvalidTokenException("Token not found")
    }

    private fun getBearerToken(req: RequestWrapper): String? {
        return req.getHeaderValue("Authorization")
                ?.let {
                    if (!it.startsWith(BEARER_PREFIX)) {
                        throw InvalidTokenException("Not bearer token")
                    }
                    it.replace(BEARER_PREFIX, "")
                }
    }
}