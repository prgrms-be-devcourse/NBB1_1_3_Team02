package com.example.bookYourSeat.aop.logtracer

data class TraceStatus(
    val traceId: TraceId,
    val startTimesMs: Long,
    val message: String
)
