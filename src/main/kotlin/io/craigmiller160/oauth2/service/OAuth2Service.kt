package io.craigmiller160.oauth2.service

import io.craigmiller160.oauth2.dto.AuthUserDto

interface OAuth2Service {
    // Returns cookie string to clear auth token
    fun logout(): String

    fun getAuthenticatedUser(): AuthUserDto
}