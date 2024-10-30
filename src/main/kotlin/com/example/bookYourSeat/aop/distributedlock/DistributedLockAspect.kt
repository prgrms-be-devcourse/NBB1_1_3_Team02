package com.example.bookYourSeat.aop.distributedlock

import java.lang.reflect.Method
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class DistributedLockAspect(private val redisson: RedissonClient) {

    @Around("@annotation(com.example.bookYourSeat.aop.distributedlock.DistributedLock)")
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method: Method = signature.method
        val distributedLock = method.getAnnotation(DistributedLock::class.java)

        val couponId = joinPoint.args[1] as Long
        val lockKey = distributedLock.key

        val lock: RLock = redisson.getLock("$lockKey$couponId")
        try {
            val acquired = lock.tryLock(
                distributedLock.waitTime,
                distributedLock.leaseTime,
                distributedLock.timeUnit
            )
            logger.info("get lock")
            if (!acquired) {
                throw IllegalArgumentException()
            }
            return joinPoint.proceed()
        } finally {
            lock.unlock()
            logger.info("lock released")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DistributedLockAspect::class.java)
    }
}
