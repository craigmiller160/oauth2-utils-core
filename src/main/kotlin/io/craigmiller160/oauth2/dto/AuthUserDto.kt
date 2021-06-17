package io.craigmiller160.oauth2.dto

// TODO need to use this for the jaxrs version
data class AuthUserDto(
        val username: String,
        val roles: List<String>,
        val firstName: String,
        val lastName: String
)
