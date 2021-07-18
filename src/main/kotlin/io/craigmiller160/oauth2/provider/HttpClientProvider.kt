package io.craigmiller160.oauth2.provider

import java.net.http.HttpClient

fun interface HttpClientProvider : OAuth2Provider<HttpClient>