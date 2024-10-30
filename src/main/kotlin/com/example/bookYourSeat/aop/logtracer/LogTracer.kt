package com.example.bookYourSeat.aop.logtracer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LogTracer {

    private val traceIdHolder = ThreadLocal<TraceId>()

    fun begin(message: String, args: String): TraceStatus {
        syncTraceId()
        val traceId = traceIdHolder.get()
        val startTimeMs = System.currentTimeMillis()
        if (traceId.isFirstLevel()) {
            logger.info("--------------------- [TraceId: {}] start ---------------------", traceId.id)
        }
        logger.info("[{}] {}{}, args = {}", traceId.id, addSpace(PREFIX, traceId.level), message, args)
        return TraceStatus(traceId, startTimeMs, message)
    }

    private fun syncTraceId() {
        val traceId = traceIdHolder.get()
        if (traceId == null) {
            traceIdHolder.set(TraceId())
        } else {
            traceIdHolder.set(traceId.createNextId())
        }
    }

    fun end(traceStatus: TraceStatus) {
        complete(traceStatus, null)
    }

    fun handleException(traceStatus: TraceStatus, ex: Exception) {
        complete(traceStatus, ex)
    }

    private fun complete(traceStatus: TraceStatus, ex: Exception?) {
        val stopTimeMs = System.currentTimeMillis()
        val resultTimeMs = stopTimeMs - traceStatus.startTimesMs
        val traceId = traceStatus.traceId
        if (ex == null) {
            logger.info("[{}] {}{} time = {}ms", traceId.id, addSpace(SUFFIX, traceId.level),
                traceStatus.message, resultTimeMs)
        } else {
            logger.info("[{}] {} {} time = {}ms ex={}", traceId.id, addSpace(ERR_FIX, traceId.level),
                traceStatus.message, resultTimeMs, ex.toString())
        }
        if (traceStatus.traceId.isFirstLevel()) {
            logger.info("--------------------- [TraceId: {}] end Time: {} ms ---------------------",
                traceId.id, resultTimeMs)
        }
        releaseTraceId()
    }

    private fun releaseTraceId() {
        val traceId = traceIdHolder.get()
        if (traceId.isFirstLevel()) {
            traceIdHolder.remove()
        } else {
            traceIdHolder.set(traceId.createPrevId())
        }
    }

    private fun addSpace(prefix: String, level: Int): String {
        return buildString {
            repeat(level) { i ->
                append(if (i == level - 1) "|$prefix" else "|   ")
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LogTracer::class.java)
        private const val PREFIX = "-->"
        private const val SUFFIX = "<--"
        private const val ERR_FIX = "<X-"
    }
}
