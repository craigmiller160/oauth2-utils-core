package io.craigmiller160.oauth2.domain.repository.impl

import io.craigmiller160.oauth2.domain.SqlConnectionProvider
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import org.h2.tools.Server
import org.junit.jupiter.api.*
import java.nio.file.Files
import java.sql.DriverManager

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

    private val tempFile = Files.createTempFile("prefix", "suffix")
    private lateinit var repo: AppRefreshTokenRepositoryImpl

    private fun getJdbcUrl(): String {
        val schema = javaClass.classLoader.getResource("schema.sql").toURI().path
        return "jdbc:h2:${server.url}/${tempFile.toFile().absolutePath};MODE=PostgreSQL;INIT=RUNSCRIPT FROM '$schema'"
    }

    @BeforeEach
    fun setup() {
        val connProvider = SqlConnectionProvider {
            DriverManager.getConnection(getJdbcUrl())
        }
        repo = AppRefreshTokenRepositoryImpl(connProvider)
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
        TODO("Finish this")
    }

    @Test
    fun `removeByTokenId()`() {
        TODO("Finish this")
    }

    @Test
    fun `findByTokenId()`() {
        TODO("Finish this")
    }

    @Test
    fun `save() new token`() {
        TODO("Finish this")
    }

    @Test
    fun `save() existing token`() {
        TODO("Finish this")
    }

}