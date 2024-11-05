package com.example.bookYourSeat.aop.seatLock

import org.aspectj.lang.ProceedingJoinPoint
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
open class AopForTransaction {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    open fun proceed(joinPoint: ProceedingJoinPoint): Any? {
        return joinPoint.proceed()
    }
}
