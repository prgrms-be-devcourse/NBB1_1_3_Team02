package com.example.bookYourSeat.queue.repository

import com.example.bookYourSeat.queue.QueueConst.PROCESSING_QUEUE_KEY
import com.example.bookYourSeat.queue.QueueConst.PROCESSING_QUEUE_SIZE
import com.example.bookYourSeat.queue.QueueConst.PROCESSING_TOKEN_EXPIRATION_TIME
import com.example.bookYourSeat.queue.QueueConst.WAITING_QUEUE_KEY
import com.example.bookYourSeat.queue.QueueConst.WAITING_TOKEN_EXPIRATION_TIME
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Repository
import java.io.Serializable

@Repository
class QueueRedisRepository(
    private val zSet: ZSetOperations<String, String>
) : Serializable {

    fun enqueueProcessingQueue(userId: Long, token: String) {
        val value = generateValue(userId, token)
        zSet.add(PROCESSING_QUEUE_KEY, value,
            (System.currentTimeMillis() + PROCESSING_TOKEN_EXPIRATION_TIME).toDouble()
        )
    }

    fun enqueueWaitingQueue(userId: Long, token: String) {
        val value = generateValue(userId, token)
        zSet.add(WAITING_QUEUE_KEY, value, (System.currentTimeMillis() + WAITING_TOKEN_EXPIRATION_TIME).toDouble())
    }

    val isProcessableNow: Boolean
        /*
    바로 Processing 가능한지 여부 반환
     */
        get() {
            var pqSize: Long? = zSet.zCard(PROCESSING_QUEUE_KEY)
            var wqSize: Long? = zSet.zCard(WAITING_QUEUE_KEY)
            if (pqSize == null) pqSize = 0L
            if (wqSize == null) wqSize = 0L

            return pqSize < PROCESSING_QUEUE_SIZE && wqSize == 0L
        }

    fun isInProcessingQueue(userId: Long): Boolean {
        val values: MutableSet<String> = zSet.range(PROCESSING_QUEUE_KEY, 0, -1) ?: throw IllegalArgumentException("")
        return values.stream().anyMatch { value: String -> value.matches("$userId:.*".toRegex()) }
    }

    fun isInWaitingQueue(userId: Long): Boolean {
        val values: MutableSet<String> = zSet.range(WAITING_QUEUE_KEY, 0, -1) ?: throw IllegalArgumentException("")
        return values.stream().anyMatch { value: String -> value.matches("$userId:.*".toRegex()) }
    }

    /*
    몇번째로 대기하고 있는지를 반환
     */
    fun getWaitingQueuePosition(userId: Long, token: String): Int {
        val value = generateValue(userId, token)
        val rank: Long = zSet.rank(WAITING_QUEUE_KEY, value) ?: throw IllegalArgumentException("")
        return rank.toInt() + 1
    }

    val processingQueueCount: Int
        /*
    대기열에 존재 인원 수 반환
     */
        get() {
            val count: Long? = zSet.zCard(PROCESSING_QUEUE_KEY)
            return if ((count == null)) 0 else count.toInt()
        }

    /*
    업데이트 되어야 하는 인원을 반환
     */
    fun getFrontTokensFromWaitingQueue(count: Int): List<String> {
        val tokens: MutableSet<String>? = zSet.range(WAITING_QUEUE_KEY, 0, (count - 1).toLong())
        return ArrayList(tokens)
    }

    /*
    대기열에서 처리열로 이동
     */
    fun updateWaitingToProcessing(value: String?) {
        zSet.remove(WAITING_QUEUE_KEY, value)
        if (value != null) {
            zSet.add(PROCESSING_QUEUE_KEY, value,
                (System.currentTimeMillis() + PROCESSING_TOKEN_EXPIRATION_TIME).toDouble()
            )
        }
    }

    /*
    웨이팅 취소
     */
    fun removeTokenInWaitingQueue(userId: Long, token: String) {
        val value = generateValue(userId, token)
        zSet.remove(WAITING_QUEUE_KEY, value)
    }

    /*
    유효시간 만료 토큰 삭제
     */
    fun removeExpiredToken(currentTime: Long) {
        zSet.removeRangeByScore(PROCESSING_QUEUE_KEY, Double.NEGATIVE_INFINITY, currentTime.toDouble())
        zSet.removeRangeByScore(WAITING_QUEUE_KEY, Double.NEGATIVE_INFINITY, currentTime.toDouble())
    }

    /*
    처리열에 있는 토큰 삭제 (API 완료 시, 예외 발생 시)
     */
    fun removeTokenInProcessingQueue(userId: Long, token: String) {
        val value = generateValue(userId, token)
        zSet.remove(PROCESSING_QUEUE_KEY, value)
    }

    private fun generateValue(userId: Long, token: String): String {
        return "$userId:$token"
    }
}
