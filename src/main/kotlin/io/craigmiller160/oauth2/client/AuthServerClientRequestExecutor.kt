package io.craigmiller160.oauth2.client

interface AuthServerClientRequestExecutor<T> {

    fun executeRequest(req: AuthServerClientRequest): T

}