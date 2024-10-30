package com.example.bookYourSeat.service.tossPayment

import com.example.bookYourSeat.IntegralTestSupport
import com.example.bookYourSeat.payment.controller.dto.request.TossConfirmRequest
import com.example.bookYourSeat.payment.controller.dto.response.TossConfirmResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

class TossPaymentTest : IntegralTestSupport() {

    @Test
    @DisplayName("토스페이먼츠 결재 확정 api 성공")
    fun testConfirm() {
        //given
        val request = TossConfirmRequest(
            "paymentKey", "orderId", 1000L
        )
        val mockResponse = TossConfirmResponse(
            "orderId", 1000L, "paymentKey", LocalDateTime.now()
        )

        `when`(tossApiService.confirm(request)).thenReturn(mockResponse)

        //when
        val response = tossApiService.confirm(request)

        //then
        assertThat(response, equalTo(mockResponse))
    }

    @Test
    @DisplayName("토스페이먼츠 결재 확정 api 실패")
    fun testConfirmException() {
        //given
        val request = TossConfirmRequest(
            "paymentKey", "orderId", 1000L
        )

        `when`(tossApiService.confirm(request))
            .thenThrow(IllegalArgumentException::class.java)

        //then
        assertFailsWith<IllegalArgumentException> {
            tossApiService.confirm(request)
        }
    }
}
