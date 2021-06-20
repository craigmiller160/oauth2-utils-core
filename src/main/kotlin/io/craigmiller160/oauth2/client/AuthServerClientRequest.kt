package io.craigmiller160.oauth2.client

data class AuthServerClientRequest(
        val url: String,
        val headers: Map<String,String>,
        val body: Map<String,String>
)
