package io.craigmiller160.oauth2.security

interface AuthenticationFilterService {
    fun authenticateRequest(req: RequestWrapper): Result<Unit>
    fun getInsecurePathPatterns(): List<String>
}