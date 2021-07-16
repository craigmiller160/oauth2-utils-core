package io.craigmiller160.oauth2.security

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object CookieCreator {

    private const val DEFAULT_MAX_AGE = 24 * 60 * 60

    val cookieExpFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")

    fun createTokenCookie(cookieName: String, token: String, cookiePath: String, maxAgeSecs: Int = DEFAULT_MAX_AGE): String {
        val expires = ZonedDateTime.now(ZoneId.of("GMT"))
                .plusHours(24)
        val expiresString = cookieExpFormat.format(expires)

        return "$cookieName=$token; Max-Age=$maxAgeSecs; Expires=$expiresString; Secure; HttpOnly; SameSite=strict; Path=$cookiePath"
    }

}