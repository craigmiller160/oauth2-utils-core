package io.craigmiller160.oauth2.provider

import java.sql.Connection

fun interface SqlConnectionProvider : OAuth2Provider<Connection>