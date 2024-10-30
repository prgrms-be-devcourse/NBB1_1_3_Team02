package com.example.bookYourSeat.queue.service

import com.example.bookYourSeat.queue.QueueConst.ALREADY_ISSUED_USER
import com.example.bookYourSeat.queue.QueueConst.PROCESSING_QUEUE_SIZE
import com.example.bookYourSeat.queue.QueueConst.REMOVE_BAD_REQUEST
import com.example.bookYourSeat.queue.repository.QueueRedisRepository
import com.example.bookYourSeat.queue.util.QueueJwtUtil
import org.springframework.stereotype.Service

@Service
class QueueCommandServiceImpl(
    private val queueRedisRepository: QueueRedisRepository,
    private val queueJwtUtil: QueueJwtUtil
) : QueueCommandService {

    /*
    토큰을 발급하고, Waiting Queue에 등록
     */
    override fun issueTokenAndEnqueue(userId: Long): String {
        val token: String = queueJwtUtil.createJwt(userId)

        checkAlreadyIssuedUser(userId)

        //waiting queue가 비어있고, processing queue가 다 차지 않았으면 바로 processing queue에 넣어주기
        if (queueRedisRepository.isProcessableNow) {
            queueRedisRepository.enqueueProcessingQueue(userId, token)
        } else {
            queueRedisRepository.enqueueWaitingQueue(userId, token)
        }

        return token
    }

    /*
    Waiting Queue -> Processing Queue 로 이동시키기
     */
    override fun updateWaitingToProcessing() {
        val count = calculateAvailableProcessingCount()
        if (count == 0) return

        val values: List<String> = queueRedisRepository.getFrontTokensFromWaitingQueue(count)
        values.forEach(queueRedisRepository::updateWaitingToProcessing)
    }

    /*
    스케줄러 실행 시 만료토큰 제거
     */
    override fun removeExpiredToken() {
        queueRedisRepository.removeExpiredToken(System.currentTimeMillis())
    }

    /*
    대기열에서 토큰 삭제
     */
    override fun removeTokenInWaitingQueue(userId: Long, token: String) {
        val tokenUserId: Long = queueJwtUtil.getUserIdByToken(token)
        if (userId == tokenUserId) queueRedisRepository.removeTokenInWaitingQueue(userId, token)
        else throw IllegalArgumentException(REMOVE_BAD_REQUEST)
    }

    /*
    처리열에서 토큰 삭제 (API 완료 시, 예외 발생 시)
   */
    override fun removeTokenInProcessingQueue(userId: Long, token: String) {
        val tokenUserId: Long = queueJwtUtil.getUserIdByToken(token)
        if (userId == tokenUserId) queueRedisRepository.removeTokenInProcessingQueue(userId, token)
        else throw IllegalArgumentException(REMOVE_BAD_REQUEST)
    }

    /*
    이미 토큰이 발급된 유저인지 확인
     */
    private fun checkAlreadyIssuedUser(userId: Long) {
        if (queueRedisRepository.isInWaitingQueue(userId)
            || queueRedisRepository.isInProcessingQueue(userId)
        ) {
            throw IllegalArgumentException(ALREADY_ISSUED_USER)
        }
    }

    /*
    update 가능한 queue Size 계산
     */
    private fun calculateAvailableProcessingCount(): Int {
        val size: Int = queueRedisRepository.processingQueueCount
        return PROCESSING_QUEUE_SIZE - size
    }
}
