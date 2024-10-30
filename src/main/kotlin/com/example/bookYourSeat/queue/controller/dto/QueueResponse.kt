package com.example.bookYourSeat.queue.controller.dto

import com.example.bookYourSeat.queue.QueueConst.NOT_IN_QUEUE
import com.example.bookYourSeat.queue.QueueConst.PROCESSING
import com.example.bookYourSeat.queue.QueueConst.WAITING
import com.example.bookYourSeat.queue.QueueConst.ZERO

data class QueueResponse(
    val status: String,
    val waitingQueueCount: Int,
    val estimatedWaitTime: Int
) {
    companion object {
        fun processing(): QueueResponse {
            return QueueResponse(PROCESSING, ZERO, ZERO)
        }

        fun waiting(position: Int, estimatedWaitTime: Int): QueueResponse {
            return QueueResponse(WAITING, position, estimatedWaitTime)
        }

        fun notInQueue(): QueueResponse {
            return QueueResponse(NOT_IN_QUEUE, ZERO, ZERO)
        }
    }
}
