package io.craigmiller160.oauth2.service

import io.craigmiller160.oauth2.security.AuthenticatedUser

fun interface AuthUserProvider {
    fun provide(): AuthenticatedUser
}