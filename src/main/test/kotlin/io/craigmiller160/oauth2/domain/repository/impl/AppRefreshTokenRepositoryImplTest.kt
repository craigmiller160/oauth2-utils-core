package io.craigmiller160.oauth2.domain.repository.impl

import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.domain.SqlConnectionProvider
import io.craigmiller160.oauth2.domain.entity.AppRefreshToken
import org.h2.tools.Server
import org.junit.jupiter.api.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.nio.file.Files
import java.sql.DriverManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AppRefreshTokenRepositoryImplTest {

    companion object {
        private const val TOKEN_ID = "tokenId"
        private const val REFRESH_TOKEN = "refreshToken"

        lateinit var server: Server

        @BeforeAll
        @JvmStatic
        fun createServer() {
            server = Server.createTcpServer("-ifNotExists")
            server.start()
        }

        @AfterAll
        @JvmStatic
        fun destroyServer() {
            server.stop()
        }
    }

    @Mock
    private lateinit var oAuth2Config: OAuth2Config

    private val tempFile = Files.createTempFile("prefix", "suffix")
    private lateinit var repo: AppRefreshTokenRepositoryImpl

    private fun getJdbcUrl(): String {
        val schema = javaClass.classLoader.getResource("schema.sql").toURI().path
        return "jdbc:h2:${server.url}/${tempFile.toFile().absolutePath};MODE=PostgreSQL;INIT=RUNSCRIPT FROM '$schema'"
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        `when`(oAuth2Config.getOrDefaultSchemaName())
                .thenReturn(OAuth2Config.SCHEMA)
        val connProvider = SqlConnectionProvider {
            DriverManager.getConnection(getJdbcUrl())
        }
        repo = AppRefreshTokenRepositoryImpl(connProvider, oAuth2Config)
    }

    @AfterEach
    fun clean() {
        DriverManager.getConnection(getJdbcUrl()).use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeUpdate("DELETE FROM app_refresh_tokens")
            }
        }
    }

    @Test
    fun `deleteById()`() {
        val id: Long = DriverManager.getConnection(getJdbcUrl()).use { conn ->
            conn.prepareStatement("INSERT INTO app_refresh_tokens (token_id, refresh_token) VALUES(?,?)").use { stmt ->
                stmt.setString(1, TOKEN_ID)
                stmt.setString(2, REFRESH_TOKEN)
                stmt.executeUpdate()
            }
            conn.createStatement().use { stmt -> stmt.executeQuery("SELECT * FROM app_refresh_tokens").use { rs ->
                rs.next()
                rs.getLong("id")
            } }
        }

        repo.deleteById(id)

        DriverManager.getConnection(getJdbcUrl()).use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM app_refresh_tokens").use { rs ->
                    assertTrue { rs.next() }
                    assertEquals(0, rs.getLong(1))
                }
            }
        }
    }

    @Test
    fun `removeByTokenId()`() {
        DriverManager.getConnection(getJdbcUrl()).use { conn ->
            conn.prepareStatement("INSERT INTO app_refresh_tokens (token_id, refresh_token) VALUES(?,?)").use { stmt ->
                stmt.setString(1, TOKEN_ID)
                stmt.setString(2, REFRESH_TOKEN)
                stmt.executeUpdate()
            }
        }

        repo.removeByTokenId(TOKEN_ID)

        DriverManager.getConnection(getJdbcUrl()).use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM app_refresh_tokens").use { rs ->
                    assertTrue { rs.next() }
                    assertEquals(0, rs.getLong(1))
                }
            }
        }
    }

    @Test
    fun `findByTokenId()`() {
        DriverManager.getConnection(getJdbcUrl()).use { conn ->
            conn.prepareStatement("INSERT INTO app_refresh_tokens (token_id, refresh_token) VALUES(?,?)").use { stmt ->
                stmt.setString(1, TOKEN_ID)
                stmt.setString(2, REFRESH_TOKEN)
                stmt.executeUpdate()
            }
        }

        val result = repo.findByTokenId(TOKEN_ID)
        assertNotNull(result)
        assertTrue { result.id > 0 }
        assertEquals(TOKEN_ID, result.tokenId)
        assertEquals(REFRESH_TOKEN, result.refreshToken)
    }

    @Test
    fun `save() new token`() {
        val token = AppRefreshToken(
                id = 0,
                tokenId = TOKEN_ID,
                refreshToken = REFRESH_TOKEN
        )
        val result = repo.save(token)
        assertEquals(token.copy(id = result.id), result)

        DriverManager.getConnection(getJdbcUrl()).use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM app_refresh_tokens").use { rs ->
                    assertTrue { rs.next() }
                    assertEquals(1, rs.getLong(1))
                }

                stmt.executeQuery("SELECT * FROM app_refresh_tokens").use { rs ->
                    assertTrue { rs.next() }
                    assertEquals(result.id, rs.getLong("id"))
                    assertEquals(result.tokenId, rs.getString("token_id"))
                    assertEquals(result.refreshToken, rs.getString("refresh_token"))
                }
            }
        }
    }

    @Test
    fun `save() existing token`() {
        val id: Long = DriverManager.getConnection(getJdbcUrl()).use { conn ->
            conn.prepareStatement("INSERT INTO app_refresh_tokens (token_id, refresh_token) VALUES(?,?)").use { stmt ->
                stmt.setString(1, TOKEN_ID)
                stmt.setString(2, REFRESH_TOKEN)
                stmt.executeUpdate()
            }
            conn.createStatement().use { stmt -> stmt.executeQuery("SELECT * FROM app_refresh_tokens").use { rs ->
                rs.next()
                rs.getLong("id")
            } }
        }

        val token = AppRefreshToken(
                id = id,
                tokenId = "${TOKEN_ID}_2",
                refreshToken = "${REFRESH_TOKEN}_2"
        )
        val result = repo.save(token)
        assertEquals(token, result)

        DriverManager.getConnection(getJdbcUrl()).use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT COUNT(*) FROM app_refresh_tokens").use { rs ->
                    assertTrue { rs.next() }
                    assertEquals(1, rs.getLong(1))
                }

                stmt.executeQuery("SELECT * FROM app_refresh_tokens").use { rs ->
                    assertTrue { rs.next() }
                    assertEquals(result.id, rs.getLong("id"))
                    assertEquals(result.tokenId, rs.getString("token_id"))
                    assertEquals(result.refreshToken, rs.getString("refresh_token"))
                }
            }
        }
    }

}