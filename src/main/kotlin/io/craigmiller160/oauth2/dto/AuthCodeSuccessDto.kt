package io.craigmiller160.oauth2.dto

data class AuthCodeSuccessDto(
        val cookie: String,
        val postAuthRedirect: String
)
