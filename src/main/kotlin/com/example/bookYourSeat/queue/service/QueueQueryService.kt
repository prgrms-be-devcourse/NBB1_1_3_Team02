package com.example.bookYourSeat.queue.service

import com.example.bookYourSeat.queue.controller.dto.QueueResponse

interface QueueQueryService {
    fun findQueueStatus(userId: Long, token: String): QueueResponse
}
