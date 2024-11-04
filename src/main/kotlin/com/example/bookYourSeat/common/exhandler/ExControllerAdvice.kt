package com.example.bookYourSeat.common.exhandler

import com.example.bookYourSeat.common.entity.ErrorResult
import com.example.bookYourSeat.common.service.SlackFacade
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExControllerAdvice(
    private val slackFacade: SlackFacade
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(IllegalArgumentException::class)
    fun catchError(e: IllegalArgumentException, request: HttpServletRequest): ResponseEntity<ErrorResult> {
        log.error("[exceptionHandle] ex", e)

        val errorResult = ErrorResult(HttpStatus.BAD_REQUEST.name, e.message ?: "")
        slackFacade.sendSlackErrorMessage(errorResult, request)
        return ResponseEntity(errorResult, HttpStatus.BAD_REQUEST)
    }
}
