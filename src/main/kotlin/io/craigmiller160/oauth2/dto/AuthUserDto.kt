package io.craigmiller160.oauth2.dto

data class AuthUserDto(
        val username: String,
        val roles: List<String>,
        val firstName: String,
        val lastName: String
)
