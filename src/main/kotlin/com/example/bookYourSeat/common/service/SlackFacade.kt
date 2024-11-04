package com.example.bookYourSeat.common.service

import com.example.bookYourSeat.common.constants.Constants.EXCEPTION_DATETIME
import com.example.bookYourSeat.common.constants.Constants.EXCEPTION_LOG
import com.example.bookYourSeat.common.constants.Constants.EXCEPTION_METHOD
import com.example.bookYourSeat.common.constants.Constants.EXCEPTION_URL
import com.example.bookYourSeat.common.constants.Constants.TIME
import com.example.bookYourSeat.common.entity.ErrorResult
import com.example.bookYourSeat.common.entity.color.Color
import com.example.bookYourSeat.payment.PaymentConst.AMOUNT_PAY
import com.example.bookYourSeat.payment.PaymentConst.PAY_TIME
import com.example.bookYourSeat.payment.controller.dto.response.ConfirmResponse
import com.example.bookYourSeat.reservation.ReservationConst.RESERVATION_NUMBER
import com.example.bookYourSeat.reservation.ReservationConst.RESERVATION_SEAT
import com.example.bookYourSeat.reservation.ReservationConst.RESERVATION_STATE
import com.example.book_your_seat.concert_kotlin.ConcertConst.CONCERT_NAME
import com.example.book_your_seat.concert_kotlin.ConcertConst.CONCERT_TIME
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class SlackFacade(
    private val slackService: SlackService
) {

    fun sendSlackErrorMessage(errorResult: ErrorResult, request: HttpServletRequest) {
        val dataMap = LinkedHashMap<String, String>()

        dataMap[EXCEPTION_LOG] = errorResult.message
        dataMap[EXCEPTION_URL] = request.requestURL.toString()
        dataMap[EXCEPTION_DATETIME] = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME))
        dataMap[EXCEPTION_METHOD] = request.method

        slackService.setMessage(errorResult.code, dataMap, Color.RED)
    }

    fun sendPaymentSuccessMessage(confirmResponse: ConfirmResponse) {
        val dataMap = LinkedHashMap<String, String>()
        dataMap[RESERVATION_NUMBER] = confirmResponse.reservationId.toString()
        dataMap[AMOUNT_PAY] = confirmResponse.concludePrice.toString()
        dataMap[RESERVATION_STATE] = confirmResponse.status.toString()
        dataMap[CONCERT_NAME] = confirmResponse.concertTitle
        dataMap[CONCERT_TIME] = confirmResponse.concertStartHour.toString()
        dataMap[PAY_TIME] = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME))

        val seatNumber = seatNumberConcat(confirmResponse)

        dataMap[RESERVATION_SEAT] = seatNumber

        slackService.setMessage("결제 완료!", dataMap, Color.BLUE)
    }

    private fun seatNumberConcat(confirmResponse: ConfirmResponse): String {
        val seatsId = confirmResponse.seatNumbers
        val stringBuilder = StringBuilder()

        for (seatId in seatsId) {
            stringBuilder.append("좌석 번호: ").append(seatId).append(" ")
        }

        return stringBuilder.toString()
    }
}
