package io.craigmiller160.oauth2.security

import org.junit.jupiter.api.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals

class CookieCreatorTest {

    private val token = "Token"
    private val cookieName = "Name"
    private val cookiePath = "Path"

    @Test
    fun `create cookie`() {
        val expires = ZonedDateTime.now(ZoneId.of("GMT"))
                .plusHours(24)
        val cookie = CookieCreator().createTokenCookie(cookieName, cookiePath, token)
        val expiresString = CookieCreator().cookieExpFormat.format(expires)
        // There is still a timing risk here that can cause this test to fail...
        assertEquals("$cookieName=$token; Max-Age=86400; Expires=$expiresString; Secure; HttpOnly; SameSite=strict; Path=$cookiePath", cookie)
    }

}