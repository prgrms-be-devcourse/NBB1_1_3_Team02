package com.example.bookYourSeat.queue.service.facade

import com.example.bookYourSeat.queue.controller.dto.QueueResponse
import com.example.bookYourSeat.queue.controller.dto.TokenResponse

interface QueueService {
    fun issueTokenAndEnqueue(userId: Long): TokenResponse
    fun dequeueWaitingQueue(userId: Long, token: String)
    fun dequeueProcessingQueue(userId: Long, token: String)
    fun findQueueStatus(userId: Long, token: String): QueueResponse
    fun updateWaitingToProcessing()
}
