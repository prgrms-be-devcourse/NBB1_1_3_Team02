package com.example.bookYourSeat.aop.querycounter

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import org.slf4j.LoggerFactory

class ConnectionHandler(
    private val target: Any,
    private val counter: QueryCounter
) : InvocationHandler {

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        countPrepareStatement(method)
        logQueryCount(method)
        return method.invoke(target, *(args ?: emptyArray()))
    }

    private fun logQueryCount(method: Method) {
        if (method.name == "close") {
            warnTooManyQuery()
            logger.info("====== 발생한 쿼리 수 : {} =======\n", counter.count)
        }
    }

    private fun countPrepareStatement(method: Method) {
        if (method.name == "prepareStatement") {
            counter.increase()
        }
    }

    private fun warnTooManyQuery() {
        if (counter.isWarn) {
            logger.warn("======= Too Many Query !!!! =======")
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ConnectionHandler::class.java)
    }
}
