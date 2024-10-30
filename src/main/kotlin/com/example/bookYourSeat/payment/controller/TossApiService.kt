package com.example.bookYourSeat.payment.controller

import com.example.bookYourSeat.payment.controller.dto.request.TossConfirmRequest
import com.example.bookYourSeat.payment.controller.dto.response.TossConfirmResponse
import com.example.bookYourSeat.payment.controller.dto.response.TossErrorResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.HttpStatusCodeException
import java.util.*

@Service
class TossApiService(
    private val tossOpenFeign: TossOpenFeign,
    private val objectMapper: ObjectMapper
) {
    fun confirm(request: TossConfirmRequest): TossConfirmResponse? {
        val auth = Base64.getEncoder().encodeToString("$SECRET_KEY:".toByteArray())

        return try {
            tossOpenFeign.confirm("Basic $auth", request)
        } catch (httpException: HttpClientErrorException) {
            handleTossException(httpException)
            null
        } catch (httpException: HttpServerErrorException) {
            handleTossException(httpException)
            null
        }
    }

    private fun handleTossException(httpException: HttpStatusCodeException) {
        try {
            val response: TossErrorResponse = objectMapper.readValue(httpException.responseBodyAsString, TossErrorResponse::class.java)
            throw IllegalArgumentException(response.message)
        } catch (jsonException: JsonProcessingException) {
            throw IllegalArgumentException(jsonException.message)
        }
    }

    companion object {
        private const val SECRET_KEY = "key"
    }
}

