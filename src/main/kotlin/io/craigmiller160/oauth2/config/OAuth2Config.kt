package io.craigmiller160.oauth2.config

import com.nimbusds.jose.jwk.JWKSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL
import javax.annotation.PostConstruct


abstract class OAuth2Config {
    abstract var authServerHost: String
    abstract var authCodeRedirectUri: String
    abstract var clientName: String
    abstract var clientKey: String
    abstract var clientSecret: String
    abstract var cookieName: String
    abstract var postAuthRedirect: String
    abstract var cookieMaxAgeSecs: Long
    abstract var cookiePath: String
    abstract var authLoginBaseUri: String
    abstract var insecurePaths: String
    abstract var authCodeWaitMins: Long

    val jwkPath = "/jwk"
    val tokenPath = "/oauth/token"
    val authCodeLoginPath = "/ui/login"

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    lateinit var jwkSet: JWKSet

    fun getOrDefaultCookiePath(): String {
        if (cookiePath.isNotBlank()) {
            return cookiePath
        }
        return "/"
    }

    fun getBaseWait(): Long {
        return 1000
    }

    fun loadJWKSet(): JWKSet {
        return JWKSet.load(URL("$authServerHost$jwkPath"))
    }

    fun getInsecurePathList(): List<String> {
        return insecurePaths.split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() }
    }

    @PostConstruct
    fun tryToLoadJWKSet() {
        println("LOADING JWK SET PRINTLN") // TODO delete this
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