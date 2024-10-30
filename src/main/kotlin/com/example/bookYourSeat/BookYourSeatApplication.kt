package com.example.bookYourSeat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@EnableFeignClients(basePackages = ["com.example.bookYourSeat.payment.controller"])
@EnableCaching
open class BookYourSeatApplication

fun main(args: Array<String>) {
	runApplication<BookYourSeatApplication>(*args)
}
