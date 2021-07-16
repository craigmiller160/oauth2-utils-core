package io.craigmiller160.oauth2.security

data class AuthenticatedUser(
        val userName: String,
        val roles: List<String>,
        val firstName: String,
        val lastName: String,
        val tokenId: String
)