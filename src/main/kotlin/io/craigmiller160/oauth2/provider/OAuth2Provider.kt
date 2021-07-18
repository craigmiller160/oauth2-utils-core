package io.craigmiller160.oauth2.provider

fun interface OAuth2Provider<T> {
    fun provide(): T
}