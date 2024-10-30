package com.example.bookYourSeat.concert.controller

import com.example.bookYourSeat.concert.controller.dto.ConcertResponse
import com.example.book_your_seat.concert_kotlin.controller.dto.ResultRedisConcert
import com.example.bookYourSeat.concert.service.ConcertQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/concerts")
class ConcertController(
    private val concertQueryService: ConcertQueryService
) {
    @GetMapping
    fun findAll(): ResponseEntity<List<ConcertResponse>> {
        val responses = concertQueryService.findAll()
        return ResponseEntity.ok(responses)
    }

    @GetMapping("/{concertId}")
    fun findById(@PathVariable concertId: Long): ResponseEntity<ConcertResponse> {
        val response = concertQueryService.findById(concertId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/redis/list")
    fun findAllRedisList(): ResponseEntity<ResultRedisConcert> {
        val resultRedisConcert = concertQueryService.findUsedRedisAllConcertList()
        return ResponseEntity.ok(resultRedisConcert)
    }
}