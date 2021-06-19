package io.craigmiller160.oauth2.config

import com.nimbusds.jose.jwk.JWKSet


interface OAuth2Config {

    companion object {
        const val JWK_PATH = "/jwk"
        const val TOKEN_PATH = "/oauth/token"
        const val AUTH_CODE_LOGIN_PATH = "/ui/login"
    }

    var authServerHost: String
    var authCodeRedirectUri: String
    var clientName: String
    var clientKey: String
    var clientSecret: String
    var cookieName: String
    var postAuthRedirect: String
    var cookieMaxAgeSecs: Long
    var cookiePath: String
    var authLoginBaseUri: String
    var insecurePaths: String
    var authCodeWaitMins: Long

    var jwkSet: JWKSet

    fun getOrDefaultCookiePath(): String
    fun getBaseWait(): Long
    fun loadJWKSet(): JWKSet
    fun getInsecurePathList(): List<String>
    fun tryToLoadJWKSet()

}