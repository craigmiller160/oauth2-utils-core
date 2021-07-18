package io.craigmiller160.oauth2.provider

import io.craigmiller160.oauth2.security.AuthenticatedUser

fun interface AuthUserProvider : OAuth2Provider<AuthenticatedUser>