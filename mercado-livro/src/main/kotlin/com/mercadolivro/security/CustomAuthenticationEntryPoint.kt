package com.mercadolivro.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadolivro.controller.response.ErrorResponse
import com.mercadolivro.enums.Erros
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val error = ErrorResponse(
            message = Erros.AuthenticationTokenException.message,
            status = HttpServletResponse.SC_UNAUTHORIZED,
            timestamp = LocalDateTime.now(),
            errors = null,
            path = request.requestURI
        )
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.write(objectMapper.writeValueAsString(error))
    }
}