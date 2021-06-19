package io.craigmiller160.oauth2.config

import com.nimbusds.jose.jwk.JWKSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory


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
}