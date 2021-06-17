package io.craigmiller160.oauth2.config

interface OAuth2Config {
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
}