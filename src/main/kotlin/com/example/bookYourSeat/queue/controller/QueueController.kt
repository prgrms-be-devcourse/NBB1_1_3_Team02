package com.example.bookYourSeat.queue.controller

import com.example.bookYourSeat.queue.controller.dto.QueueResponse
import com.example.bookYourSeat.queue.controller.dto.TokenResponse
import com.example.bookYourSeat.queue.service.facade.QueueService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/queues")
class QueueController(
    private val queueService: QueueService
) {
    @PostMapping("/token")
    fun issueTokenAndEnqueue(@LoginUser user: User): ResponseEntity<TokenResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(queueService.issueTokenAndEnqueue(user.getId()))
    }

    @GetMapping
    fun getQueueInfoWithToken(
        @LoginUser user: User,
        @RequestParam("token") token: String?
    ): ResponseEntity<QueueResponse> {
        return ResponseEntity.ok()
            .body(queueService.findQueueStatus(user.getId(), token))
    }

    @PostMapping("/wait/quit")
    fun dequeueWaitingQueue(
        @LoginUser user: User,
        @RequestParam("token") token: String?
    ): ResponseEntity<Void> {
        queueService.dequeueWaitingQueue(user.getId(), token)
        return ResponseEntity.ok<Void>(null)
    }

    @PostMapping("/process/quit")
    fun dequeueProcessingQueue(
        @LoginUser user: User,
        @RequestParam("token") token: String?
    ): ResponseEntity<Void> {
        queueService.dequeueProcessingQueue(user.getId(), token)
        return ResponseEntity.ok<Void>(null)
    }
}
