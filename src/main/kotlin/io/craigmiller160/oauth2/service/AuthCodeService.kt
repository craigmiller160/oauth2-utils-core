package io.craigmiller160.oauth2.service

import io.craigmiller160.oauth2.dto.AuthCodeSuccessDto
import javax.servlet.http.HttpServletRequest

interface AuthCodeService {
    fun prepareAuthCodeLogin(req: HttpServletRequest): String

    fun code(req: HttpServletRequest, code: String, state: String): AuthCodeSuccessDto
}