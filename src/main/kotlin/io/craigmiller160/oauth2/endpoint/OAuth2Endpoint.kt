package io.craigmiller160.oauth2.endpoint

interface OAuth2Endpoint<ResponseType> {
    fun login(): ResponseType

    fun code(code: String, state: String): ResponseType

    fun logout(): ResponseType

    fun getAuthenticatedUser(): ResponseType
}