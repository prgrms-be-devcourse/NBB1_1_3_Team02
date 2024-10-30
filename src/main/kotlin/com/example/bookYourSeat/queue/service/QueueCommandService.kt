package com.example.bookYourSeat.queue.service

interface QueueCommandService {
    fun issueTokenAndEnqueue(userId: Long): String

    fun updateWaitingToProcessing()

    fun removeExpiredToken()

    fun removeTokenInWaitingQueue(userId: Long, token: String)

    fun removeTokenInProcessingQueue(userId: Long, token: String)
}
