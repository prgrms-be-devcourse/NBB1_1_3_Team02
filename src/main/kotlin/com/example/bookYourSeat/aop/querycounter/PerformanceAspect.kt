package com.example.bookYourSeat.aop.querycounter

import jakarta.servlet.http.HttpServletRequest
import java.lang.reflect.Proxy
import java.sql.Connection
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class PerformanceAspect {

    private val queryCounter = ThreadLocal<QueryCounter>()

    @Pointcut("execution(* javax.sql.DataSource.getConnection(..))")
    fun performancePointcut() {
    }

    @Around("performancePointcut()")
    fun start(point: ProceedingJoinPoint): Any? {
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        if (requestAttributes is ServletRequestAttributes) {
            val request: HttpServletRequest = requestAttributes.request
            val httpMethod = request.method
            val requestURI = request.requestURI

            logger.info("HTTP Method: {}, Request URI: {}", httpMethod, requestURI)
        } else {
            logger.info("No active HTTP request context available.")
        }

        val connection = point.proceed() as Connection
        queryCounter.set(QueryCounter())
        val counter = queryCounter.get()

        val proxyConnection = getProxyConnection(connection, counter)
        queryCounter.remove()
        return proxyConnection
    }

    private fun getProxyConnection(connection: Connection, counter: QueryCounter): Connection {
        return Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf(Connection::class.java),
            ConnectionHandler(connection, counter)
        ) as Connection
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PerformanceAspect::class.java)
    }
}
