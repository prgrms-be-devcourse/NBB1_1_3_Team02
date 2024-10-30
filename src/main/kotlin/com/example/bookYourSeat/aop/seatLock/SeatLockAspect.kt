package com.example.bookYourSeat.aop.seatLock

import com.example.bookYourSeat.seat.controller.dto.SelectSeatRequest
import com.example.bookYourSeat.seat.SeatConst.REDISSON_LOCK_KEY
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.redisson.RedissonMultiLock
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Aspect
@Component
class SeatLockAspect(
    private val redissonClient: RedissonClient,
    private val aopForTransaction: AopForTransaction
) {

    @Around("@annotation(com.example.book_your_seat.aop.seatLock.SeatLock) && args(request, ..)")
    fun lock(joinPoint: ProceedingJoinPoint, request: SelectSeatRequest): Any? {
        // 좌석 ID를 정렬하여 락 키를 생성
        val lockKeys = request.seatIds()
            .map { seatId -> REDISSON_LOCK_KEY + seatId }
            .sorted()
            .toTypedArray()

        val locks = lockKeys
            .map { redissonClient.getLock(it) }
            .toTypedArray()

        val multiLock = RedissonMultiLock(*locks)

        return tryLockAndProceed(joinPoint, multiLock)
    }

    private fun tryLockAndProceed(joinPoint: ProceedingJoinPoint, multiLock: RedissonMultiLock): Any? {
        var acquired = false
        try {
            acquired = multiLock.tryLock(2, 1, TimeUnit.SECONDS)
            if (!acquired) {
                throw IllegalArgumentException("락을 획득할 수 없습니다.")
            }

            logger.info("락 획득 성공")
            return aopForTransaction.proceed(joinPoint)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt() // 현재 스레드에 인터럽트 상태를 설정
            throw RuntimeException("락 획득 중 인터럽트 발생", e)
        } finally {
            if (acquired && multiLock.isHeldByCurrentThread) {
                multiLock.unlock()
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SeatLockAspect::class.java)
    }
}
