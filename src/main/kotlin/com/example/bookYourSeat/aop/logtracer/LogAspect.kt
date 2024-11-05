package com.example.bookYourSeat.aop.logtracer

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import java.util.*

@Aspect
@Component
class LogAspect(private val logTracer: LogTracer) {

    @Pointcut("execution(* com.example.bookYourSeat..*Controller.*(..)) || execution(* com.example.bookYourSeat..*Service.*(..)) || execution(* com.example.bookYourSeat..*Repository.*(..))")
    fun everyRequest() {}

    @Around("everyRequest()")
    fun doLog(joinPoint: ProceedingJoinPoint): Any? {
        var status: TraceStatus? = null
        var hasException = false
        try {
            status = logTracer.begin(
                " Method : " + getKeySignature(joinPoint),
                joinPoint.args.contentDeepToString()
            )
            return joinPoint.proceed()
        } catch (ex: Exception) {
            status?.let { logTracer.handleException(it, ex) }
            hasException = true
            throw ex
        } finally {
            if (!hasException) status?.let { logTracer.end(it) }
        }
    }

    private fun getKeySignature(joinPoint: ProceedingJoinPoint): String {
        val split = joinPoint.signature.toString().split("\\.".toRegex()).toTypedArray()
        val length = split.size
        val arr = Arrays.copyOfRange(split, length - 3, length)
        return "${arr[1]}.${arr[2]}"
    }
}
