package com.example.bookYourSeat.aop.logtracer

import java.util.UUID

data class TraceId(
    val id: String = createTransactionId(),
    val level: Int = 0
) {
    fun createNextId(): TraceId {
        return TraceId(id, level + 1)
    }

    fun createPrevId(): TraceId {
        return TraceId(id, level - 1)
    }

    fun isFirstLevel(): Boolean {
        return level == 0
    }

    companion object {
        private fun createTransactionId(): String {
            return UUID.randomUUID().toString().substring(0, 8)
        }
    }
}
