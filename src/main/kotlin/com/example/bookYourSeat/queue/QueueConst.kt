package com.example.bookYourSeat.queue

object QueueConst {
    const val PROCESSING: String = "PROCESSING"
    const val WAITING: String = "WAITING"
    const val NOT_IN_QUEUE: String = "NOT_IN_QUEUE"

    const val ZERO: Int = 0
    const val FIVE: Int = 5
    const val MINUTE: Int = 60
    const val PROCESSING_QUEUE_SIZE: Int = 1000
    const val PROCESSING_TOKEN_EXPIRATION_TIME: Int = 30 * 60 * 1000 //30분
    const val WAITING_TOKEN_EXPIRATION_TIME: Int = 60 * 60 * 1000 //1시간

    const val PROCESSING_QUEUE_KEY: String = "processing_queue_key"
    const val WAITING_QUEUE_KEY: String = "waiting_queue_key"
    const val ALREADY_ISSUED_USER: String = "이미 토큰을 발급받은 유저입니다."
    const val REMOVE_BAD_REQUEST: String = "로그인한 유저의 토큰이 아닙니다."

    const val NOT_IN_PROCESSING_QUEUE: String = "처리 가능한 유저가 아닙니다."
}
