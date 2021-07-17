package io.craigmiller160.oauth2.service

import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import io.craigmiller160.oauth2.security.AuthenticatedUser
import io.craigmiller160.oauth2.security.CookieCreator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AbstractOAuth2ServiceTest {

    companion object {
        const val USER_NAME = "UserName"
        val ROLES = listOf("Role")
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val TOKEN_ID = "tokenId"
        const val COOKIE_NAME = "cookieName"
        const val COOKIE_PATH = "cookiePath"
    }

    @Mock
    private lateinit var cookieCreator: CookieCreator
    @Mock
    private lateinit var oAuth2Config: OAuth2Config
    @Mock
    private lateinit var appRefreshTokenRepo: AppRefreshTokenRepository
    @InjectMocks
    private lateinit var oAuth2Service: OAuth2ServiceImpl

    @Test
    fun `logout()`() {
        `when`(oAuth2Config.cookieName)
                .thenReturn(COOKIE_NAME)
        `when`(oAuth2Config.getOrDefaultCookiePath())
                .thenReturn(COOKIE_PATH)
        oAuth2Service.logout()
        verify(appRefreshTokenRepo, times(1))
                .removeByTokenId(TOKEN_ID)
        verify(cookieCreator, times(1))
                .createTokenCookie(COOKIE_NAME, COOKIE_PATH, "", 0)
    }

    @Test
    fun `getAuthenticatedUser()`() {
        TODO("Finish this")
    }

    class OAuth2ServiceImpl(
            oAuth2Config: OAuth2Config,
            appRefreshTokenRepo: AppRefreshTokenRepository,
            cookieCreator: CookieCreator
    ) : AbstractOAuth2Service(oAuth2Config, appRefreshTokenRepo, cookieCreator) {
        override fun getAuthUserContext(): AuthenticatedUser {
            return AuthUserImpl(
                    USER_NAME,
                    ROLES,
                    FIRST_NAME,
                    LAST_NAME,
                    TOKEN_ID
            )
        }
    }

    data class AuthUserImpl(
            override val userName: String,
            override val roles: List<String>,
            override val firstName: String,
            override val lastName: String,
            override val tokenId: String
    ) : AuthenticatedUser

}