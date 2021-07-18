package io.craigmiller160.oauth2.domain

import java.sql.Connection

// TODO unify providers
fun interface SqlConnectionProvider {
    fun provide(): Connection
}