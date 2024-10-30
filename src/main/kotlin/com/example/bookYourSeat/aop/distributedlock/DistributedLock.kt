package com.example.bookYourSeat.aop.distributedlock

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.util.concurrent.TimeUnit

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
annotation class DistributedLock(
    val key: String,
    val waitTime: Long = 5,
    val leaseTime: Long = 10,
    val timeUnit: TimeUnit = TimeUnit.SECONDS
)
