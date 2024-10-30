package com.example.bookYourSeat.config.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    private val objectMapper = ObjectMapper()

    @Throws(IOException::class)
    override fun handle(
        request: HttpServletRequest, response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        val errorResponse: MutableMap<String, String?> = HashMap()
        errorResponse["message"] = SecurityConst.FORBIDDEN_MESSAGE

        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}

