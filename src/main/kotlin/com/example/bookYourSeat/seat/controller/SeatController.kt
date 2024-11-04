package com.example.book_your_seat.seat.controller

import com.example.bookYourSeat.config.security.auth.LoginUser
import com.example.bookYourSeat.user.domain.User
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse
import com.example.book_your_seat.seat.controller.dto.SeatResponse
import com.example.book_your_seat.seat.service.facade.SeatFacade
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/seats")
class SeatController(
    private val seatFacade: SeatFacade
) {

    @GetMapping("/{concertId}")
    fun findAllSeats(
        @PathVariable concertId: Long
    ): ResponseEntity<List<SeatResponse>> {
        val responses: List<SeatResponse> = seatFacade.findAllSeats(concertId)
        return ResponseEntity.ok(responses)
    }

    @PostMapping("/select")
    fun selectSeat(
        @LoginUser user: User,
        @RequestBody @Valid selectSeatRequest: SelectSeatRequest
    ): ResponseEntity<SelectSeatResponse> {
        val selectSeatResponse: SelectSeatResponse = seatFacade.selectSeat(selectSeatRequest, user.id)
        return ResponseEntity.ok(selectSeatResponse)
    }
}