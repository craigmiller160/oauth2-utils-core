package io.craigmiller160.oauth2.service.impl

import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.domain.entity.AppRefreshToken
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import io.craigmiller160.oauth2.dto.TokenResponseDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class RefreshTokenServiceImplTest {

    @Mock
    private lateinit var appRefreshTokenRepo: AppRefreshTokenRepository

    @Mock
    private lateinit var authServerClient: AuthServerClient

    @InjectMocks
    private lateinit var refreshService: RefreshTokenServiceImpl

    private val refreshToken = AppRefreshToken(
            1, "JWTID", "ABCDEFG"
    )
    private val tokenResponse = TokenResponseDto(
            accessToken = "DEFG",
            refreshToken = "ABCD",
            tokenId = "ID2"
    )
    private val token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImZpcnN0TmFtZSI6ImZpcnN0TmFtZSIsImxhc3ROYW1lIjoibGFzdE5hbWUiLCJjbGllbnRLZXkiOiJjbGllbnRLZXkiLCJjbGllbnROYW1lIjoiY2xpZW50TmFtZSIsInJvbGVzIjpbIlJPTEVfMSIsIlJPTEVfMiJdLCJleHAiOjE1OTQyMzg1NDQsImlhdCI6MTU5NDIzNzk0NCwianRpIjoiSldUSUQifQ.C5su_Tp1Q4ID72NgPTvG4DqipUyj7-Nh0j-zMZdLTSKb5WOzwVSpTMCPz0ipsJCqumP_OXwCt0oViquoP-b1khdabXmx5ESCoCWQXoeT9RgnYg6U-C4Yg6sB3yjXbScZ0zAEQcfq37kQx1GLJNvJZ5dDcp9YFODlppfxlfTqyV3QwBGCY1jCz8CyCZ3-IWvdB16i0gJBe81YrQn_TEUpCrsT6OLLFdRv9BKVtamJ97YGr1cksQup2riXsLNr41M3bGl4E1KD67Jbk8S8qohqkwSO3QGyj9OBvaYCrT2KsQ0YjO3GvkcsDFuT-Qp9WIBhE-5Pfov97l--ksYpNhc4yw"

    @Test
    fun `refreshToken() success`() {
        Mockito.`when`(appRefreshTokenRepo.findByTokenId(refreshToken.tokenId))
                .thenReturn(refreshToken)
        Mockito.`when`(authServerClient.authenticateRefreshToken(refreshToken.refreshToken))
                .thenReturn(tokenResponse)

        val result = refreshService.refreshToken(token)
        Assertions.assertEquals(tokenResponse, result)

        Mockito.verify(appRefreshTokenRepo, Mockito.times(1))
                .deleteById(1)
        Mockito.verify(appRefreshTokenRepo, Mockito.times(1))
                .save(AppRefreshToken(0, tokenResponse.tokenId, tokenResponse.refreshToken))
    }

    @Test
    fun `refreshToken() token not found`() {
        val result = refreshService.refreshToken(token)
        Assertions.assertNull(result)
    }

    @Test
    fun `refreshToken() had exception`() {
        Mockito.`when`(appRefreshTokenRepo.findByTokenId(refreshToken.tokenId))
                .thenReturn(refreshToken)
        Mockito.`when`(authServerClient.authenticateRefreshToken(refreshToken.refreshToken))
                .thenThrow(RuntimeException("Dying"))

        val result = refreshService.refreshToken(token)
        Assertions.assertNull(result)
    }

}