package com.example.bookYourSeat.querycounter

import org.junit.jupiter.api.Assertions.*
import com.example.bookYourSeat.aop.querycounter.QueryCounter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class QueryCounterTest {

    private lateinit var queryCounter: QueryCounter

    @BeforeEach
    fun setUp() {
        // given
        queryCounter = QueryCounter()
    }

    @Test
    @DisplayName("카운트 증가 테스트")
    fun increase_shouldIncrementCount() {
        // when
        queryCounter.increase()

        // then
        assertEquals(1, queryCounter.count)
    }

    @Test
    @DisplayName("카운트가 10 이하일 경우 경고 하지 않는다.")
    fun isWarn_shouldReturnFalseWhenCountIs10OrLess() {
        // given
        repeat(10) {
            queryCounter.increase()
        }

        // when
        val warn = queryCounter.isWarn

        // then
        assertFalse(warn)
    }

    @Test
    @DisplayName("카운트가 10 이상일 경우 경고 한다.")
    fun isWarn_shouldReturnTrueWhenCountIsGreaterThan10() {
        // given
        repeat(11) {
            queryCounter.increase()
        }

        // when
        val warn = queryCounter.isWarn

        // then
        assertTrue(warn)
    }
}
