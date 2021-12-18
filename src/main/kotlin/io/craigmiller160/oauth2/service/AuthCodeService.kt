package io.craigmiller160.oauth2.service

import io.craigmiller160.oauth2.dto.AuthCodeSuccessDto
import jakarta.servlet.http.HttpServletRequest

interface AuthCodeService {

    companion object {
        const val STATE_ATTR = "state"
        const val STATE_EXP_ATTR = "stateExp"
        const val ORIGIN = "origin"
    }

    fun prepareAuthCodeLogin(req: HttpServletRequest): String

    fun code(req: HttpServletRequest, code: String, state: String): AuthCodeSuccessDto
}