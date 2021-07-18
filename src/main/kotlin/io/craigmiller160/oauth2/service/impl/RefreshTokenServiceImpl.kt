package io.craigmiller160.oauth2.service.impl

import com.nimbusds.jwt.SignedJWT
import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.domain.entity.AppRefreshToken
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import io.craigmiller160.oauth2.dto.TokenResponseDto
import io.craigmiller160.oauth2.service.RefreshTokenService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RefreshTokenServiceImpl(
        private val appRefreshTokenRepo: AppRefreshTokenRepository,
        private val authServerClient: AuthServerClient
) : RefreshTokenService {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun refreshToken(accessToken: String): TokenResponseDto? {
        val jwt = SignedJWT.parse(accessToken)
        val claims = jwt.jwtClaimsSet
        return appRefreshTokenRepo.findByTokenId(claims.jwtid)
                ?.let { refreshToken ->
                    runCatching {
                        val tokenResponse = authServerClient.authenticateRefreshToken(refreshToken.refreshToken)
                        appRefreshTokenRepo.deleteById(refreshToken.id)
                        appRefreshTokenRepo.save(AppRefreshToken(0, tokenResponse.tokenId, tokenResponse.refreshToken))
                        tokenResponse
                    }
                            .onFailure { ex ->
                                logger.debug("Error refreshing token", ex)
                            }
                            .getOrNull()
                }
    }
}