package com.example.bookYourSeat.aop.querycounter

class QueryCounter {
    var count: Int = 0
        private set

    fun increase() {
        count++
    }

    val isWarn: Boolean
        get() = count > 10
}
