package io.craigmiller160.oauth2.client

import io.craigmiller160.oauth2.dto.TokenResponseDto

class AuthServerClientImplTest {

    private val host = "host"
    private val path = "path"
    private val key = "key"
    private val secret = "secret"
    private val redirectUri = "redirectUri"
    private val authHeader = "Basic a2V5OnNlY3JldA=="
    private val response = TokenResponseDto("access", "refresh", "id")

}