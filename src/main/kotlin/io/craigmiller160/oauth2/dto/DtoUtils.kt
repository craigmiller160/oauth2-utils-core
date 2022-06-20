package io.craigmiller160.oauth2.dto

import io.craigmiller160.oauth2.security.AuthenticatedUser

fun authenticatedUserToAuthUserDto(authUser: AuthenticatedUser): AuthUserDto =
        AuthUserDto(
                userId = authUser.userId,
                username = authUser.userName,
                firstName = authUser.firstName,
                lastName = authUser.lastName,
                roles = authUser.roles
        )