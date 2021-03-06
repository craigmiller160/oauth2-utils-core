package io.craigmiller160.oauth2.security

interface AuthenticatedUser {
    val userId: Long
    val userName: String
    val roles: List<String>
    val firstName: String
    val lastName: String
    val tokenId: String
}