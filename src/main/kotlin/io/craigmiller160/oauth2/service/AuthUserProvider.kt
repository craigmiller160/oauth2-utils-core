package io.craigmiller160.oauth2.service

import io.craigmiller160.oauth2.security.AuthenticatedUser

// TODO unify providers
fun interface AuthUserProvider {
    fun provide(): AuthenticatedUser
}