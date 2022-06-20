package io.craigmiller160.oauth2.dto

data class AuthUserDto(
        val userId: Long,
        val username: String,
        val roles: List<String>,
        val firstName: String,
        val lastName: String
)
