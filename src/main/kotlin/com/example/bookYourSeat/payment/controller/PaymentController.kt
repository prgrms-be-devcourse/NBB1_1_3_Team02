package com.example.bookYourSeat.payment.controller

import com.example.bookYourSeat.common.service.SlackFacade
import com.example.bookYourSeat.config.security.auth.LoginUser
import com.example.bookYourSeat.payment.controller.dto.request.FinalPriceRequest
import com.example.bookYourSeat.payment.controller.dto.request.TossConfirmRequest
import com.example.bookYourSeat.payment.controller.dto.response.ConfirmResponse
import com.example.bookYourSeat.payment.controller.dto.response.FinalPriceResponse
import com.example.bookYourSeat.payment.controller.dto.response.TossConfirmResponse
import com.example.bookYourSeat.payment.service.dto.PaymentCommand
import com.example.bookYourSeat.payment.service.facade.PaymentFacade
import com.example.bookYourSeat.reservation.contorller.dto.PaymentRequest
import com.example.bookYourSeat.user.domain.User
import com.example.book_your_seat.seat.service.redis.SeatRedisService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/reservations")
class PaymentController(
    private val tossApiService: TossApiService,
    private val paymentFacade: PaymentFacade,
    private val seatRedisService: SeatRedisService,
    private val slackFacade: SlackFacade
) {
    @PostMapping("/totalPrice")
    fun getTotalPrice(
        @RequestBody @Valid request: FinalPriceRequest
    ): ResponseEntity<FinalPriceResponse> {
        val finalPrice: FinalPriceResponse = paymentFacade.getFinalPrice(request)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(finalPrice)
    }

    @PostMapping("/success")
    fun confirmPayment(
        @RequestBody @Valid request: PaymentRequest,
        @LoginUser user: User,
        @RequestParam("token") token: String
    ): ResponseEntity<ConfirmResponse> {
        seatRedisService.validateSeat(request, user.id!!)

        val confirmResponse: TossConfirmResponse = tossApiService.confirm(TossConfirmRequest.from(request)) ?: throw IllegalArgumentException("")

        val command: PaymentCommand = PaymentCommand.from(request, confirmResponse)
        val response: ConfirmResponse = paymentFacade.processPayment(command, user.id, token)

        slackFacade.sendPaymentSuccessMessage(response)
        return ResponseEntity.ok(response)
    }
}
