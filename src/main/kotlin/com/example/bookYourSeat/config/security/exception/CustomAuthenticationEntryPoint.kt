package com.example.bookYourSeat.config.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val objectMapper = ObjectMapper()

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        val errorResponse: MutableMap<String, String?> = HashMap()
        errorResponse["message"] = SecurityConst.UNAUTHORIZED_MESSAGE

        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}