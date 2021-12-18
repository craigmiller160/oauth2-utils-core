package io.craigmiller160.oauth2.config

import com.nimbusds.jose.jwk.JWKSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL
import jakarta.annotation.PostConstruct

abstract class AbstractOAuth2Config : OAuth2Config {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override lateinit var jwkSet: JWKSet

    override fun getOrDefaultRefreshTokenSchema(): String {
        if (refreshTokenSchema.isNotBlank()) {
            return refreshTokenSchema
        }
        return OAuth2Config.SCHEMA
    }

    override fun getOrDefaultCookiePath(): String {
        if (cookiePath.isNotBlank()) {
            return cookiePath
        }
        return "/"
    }

    override fun getBaseWait(): Long {
        return 1000
    }

    override fun loadJWKSet(): JWKSet {
        return JWKSet.load(URL("$authServerHost${OAuth2Config.JWK_PATH}"))
    }

    override fun getInsecurePathList(): List<String> {
        return insecurePaths.split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() }
    }

    @PostConstruct
    override fun tryToLoadJWKSet() {
        log.info("Loading JWKSet")
        for (i in 0 until 5) {
            try {
                jwkSet = loadJWKSet()
                log.debug("Successfully loaded JWKSet")
                return
            } catch (ex: Exception) {
                log.error("Error loading JWKSet", ex)
                Thread.sleep(getBaseWait() * (i + 1))
            }
        }

        throw RuntimeException("Failed to load JWKSet")
    }

}