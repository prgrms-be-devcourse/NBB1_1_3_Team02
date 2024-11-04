package com.example.bookYourSeat.queue.service

import com.example.bookYourSeat.queue.QueueConst.FIVE
import com.example.bookYourSeat.queue.QueueConst.MINUTE
import com.example.bookYourSeat.queue.QueueConst.PROCESSING_QUEUE_SIZE
import com.example.bookYourSeat.queue.controller.dto.QueueResponse
import com.example.bookYourSeat.queue.repository.QueueRedisRepository
import org.springframework.stereotype.Service

@Service
class QueueQueryServiceImpl(
    private val queueRedisRepository: QueueRedisRepository
) : QueueQueryService {

    /*
    유저의 현재 큐 상태 확인
     */
    override fun findQueueStatus(userId: Long, token: String): QueueResponse {
        if (queueRedisRepository.isInProcessingQueue(userId)) {
            return QueueResponse.processing()
        }

        val position: Int = queueRedisRepository.getWaitingQueuePosition(userId, token)
        return QueueResponse.waiting(position, calculateEstimatedWaitSeconds(position))
    }

    private fun calculateEstimatedWaitSeconds(position: Int): Int {
        val batchSize: Int = PROCESSING_QUEUE_SIZE
        val batchInterval: Int = FIVE * MINUTE
        val batches = position / batchSize
        return batches * batchInterval
    }
}
