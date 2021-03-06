package io.craigmiller160.oauth2.security

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CookieCreator {

    companion object {
        private const val DEFAULT_MAX_AGE = (24 * 60 * 60).toLong()
    }

    val cookieExpFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")

    fun createTokenCookie(cookieName: String, cookiePath: String, token: String, maxAgeSecs: Long = DEFAULT_MAX_AGE): String {
        val expires = ZonedDateTime.now(ZoneId.of("GMT"))
                .plusHours(24)
        val expiresString = cookieExpFormat.format(expires)

        return "$cookieName=$token; Max-Age=$maxAgeSecs; Expires=$expiresString; Secure; HttpOnly; SameSite=strict; Path=$cookiePath"
    }

}