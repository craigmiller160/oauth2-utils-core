package io.craigmiller160.oauth2.domain.repository.impl

import io.craigmiller160.oauth2.config.OAuth2Config
import io.craigmiller160.oauth2.domain.SqlConnectionProvider
import io.craigmiller160.oauth2.domain.entity.AppRefreshToken
import io.craigmiller160.oauth2.domain.repository.AppRefreshTokenRepository
import java.sql.Connection
import java.sql.ResultSet

class AppRefreshTokenRepositoryImpl (
        private val oAuth2Config: OAuth2Config,
        private val sqlConnectionProvider: SqlConnectionProvider
) : AppRefreshTokenRepository {
    private val deleteById = """
        DELETE FROM app_refresh_tokens
        WHERE id = ?
    """.trimIndent()
    private val selectByTokenId = """
        SELECT *
        FROM app_refresh_tokens
        WHERE token_id = ?
    """.trimIndent()
    private val deleteByTokenId = """
        DELETE FROM app_refresh_tokens
        WHERE token_id = ?
    """.trimIndent()
    private val insertRefreshToken = """
        INSERT INTO app_refresh_tokens (token_id, refresh_token)
        VALUES (?,?)
    """.trimIndent()
    private val updateRefreshToken = """
        UPDATE app_refresh_tokens 
        SET token_id = ?, refresh_token = ?
        WHERE id = ?
    """.trimIndent()

    private fun resultSetToEntity(rs: ResultSet): AppRefreshToken =
            AppRefreshToken(
                    id = rs.getLong("id"),
                    tokenId = rs.getString("token_id"),
                    refreshToken = rs.getString("refresh_token")
            )

    private fun doFindByTokenId(conn: Connection, tokenId: String): AppRefreshToken? {
        return conn.prepareStatement(selectByTokenId).use { stmt ->
            stmt.setString(1, tokenId)
            stmt.executeQuery().use { rs ->
                if (rs.next()) {
                    resultSetToEntity(rs)
                } else {
                    null
                }
            }
        }
    }

    private fun <T> executeSql(block: (Connection) -> T): T {
        return sqlConnectionProvider.provide().use { conn ->
            conn.autoCommit = false
            conn.createStatement().use { stmt ->
                stmt.executeUpdate("SET search_path TO ${oAuth2Config.getOrDefaultSchemaName()}")
            }
            conn.commit()
            block(conn)
        }
    }

    override fun findByTokenId(tokenId: String): AppRefreshToken? {
        return executeSql { conn ->
            doFindByTokenId(conn, tokenId)
        }
    }

    override fun removeByTokenId(tokenId: String) {
        executeSql { conn ->
            conn.prepareStatement(deleteByTokenId).use { stmt ->
                stmt.setString(1, tokenId)
                stmt.executeUpdate()
            }
            conn.commit()
        }
    }

    override fun deleteById(id: Long) {
        executeSql { conn ->
            conn.prepareStatement(deleteById).use { stmt ->
                stmt.setLong(1, id)
                stmt.executeUpdate()
            }
            conn.commit()
        }
    }

    private fun insertToken(conn: Connection, token: AppRefreshToken) {
        conn.prepareStatement(insertRefreshToken).use { stmt ->
            stmt.setString(1, token.tokenId)
            stmt.setString(2, token.refreshToken)
            stmt.executeUpdate()
        }
        conn.commit()
    }

    private fun updateToken(conn: Connection, token: AppRefreshToken) {
        conn.prepareStatement(updateRefreshToken).use { stmt ->
            stmt.setString(1, token.tokenId)
            stmt.setString(2, token.refreshToken)
            stmt.setLong(3, token.id)
            stmt.executeUpdate()
        }
        conn.commit()
    }

    override fun save(token: AppRefreshToken): AppRefreshToken {
        return executeSql { conn ->
            if (token.id > 0) {
                updateToken(conn, token)
            } else {
                insertToken(conn, token)
            }

            doFindByTokenId(conn, token.tokenId)!!
        }
    }
}