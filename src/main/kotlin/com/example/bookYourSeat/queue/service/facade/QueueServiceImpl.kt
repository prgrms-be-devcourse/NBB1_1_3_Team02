package com.example.bookYourSeat.queue.service.facade

import com.example.bookYourSeat.queue.controller.dto.QueueResponse
import com.example.bookYourSeat.queue.controller.dto.TokenResponse
import com.example.bookYourSeat.queue.service.QueueCommandService
import com.example.bookYourSeat.queue.service.QueueQueryService
import org.springframework.stereotype.Service

@Service
class QueueServiceImpl(
    private val queueCommandService: QueueCommandService,
    private val queueQueryService: QueueQueryService
) : QueueService {

    override fun issueTokenAndEnqueue(userId: Long?): TokenResponse? {
        return TokenResponse(queueCommandService.issueTokenAndEnqueue(userId))
    }

    override fun dequeueWaitingQueue(userId: Long?, token: String?) {
        queueCommandService.removeTokenInWaitingQueue(userId, token)
    }

    override fun dequeueProcessingQueue(userId: Long?, token: String?) {
        queueCommandService.removeTokenInProcessingQueue(userId, token)
    }


    override fun findQueueStatus(userId: Long?, token: String?): QueueResponse {
        return queueQueryService.findQueueStatus(userId, token)
    }

    override fun updateWaitingToProcessing() {
        queueCommandService.updateWaitingToProcessing()
    }
}
