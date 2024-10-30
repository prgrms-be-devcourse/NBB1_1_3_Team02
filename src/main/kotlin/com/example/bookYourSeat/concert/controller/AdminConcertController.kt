package com.example.book_your_seat.concert_kotlin.controller

import com.example.book_your_seat.concert_kotlin.controller.dto.AddConcertRequest
import com.example.book_your_seat.concert_kotlin.service.ConcertCommandService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/api/v1/concerts")
class AdminConcertController(
    private val concertCommandService: ConcertCommandService
) {

    @PostMapping
    fun addConcert(
        @Valid @RequestBody request: AddConcertRequest
    ): ResponseEntity<Unit> {
        concertCommandService.add(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/{concertId}")
    fun deleteById(@PathVariable concertId: Long): ResponseEntity<Unit> {
        concertCommandService.delete(concertId)
        return ResponseEntity.noContent().build()
    }
}