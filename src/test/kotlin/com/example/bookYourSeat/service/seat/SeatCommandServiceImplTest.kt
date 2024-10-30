package com.example.bookYourSeat.service.seat

import com.example.DbCleaner
import com.example.bookYourSeat.IntegralTestSupport
import com.example.bookYourSeat.concert.controller.dto.AddConcertRequest
import com.example.bookYourSeat.concert.repository.ConcertRepository
import com.example.bookYourSeat.concert.service.ConcertCommandService
import com.example.bookYourSeat.queue.service.facade.QueueService
import com.example.bookYourSeat.seat.controller.dto.SelectSeatRequest
import com.example.bookYourSeat.seat.domain.Seat
import com.example.bookYourSeat.seat.repository.SeatRepository
import com.example.bookYourSeat.seat.service.facade.SeatFacade
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate

import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

import kotlin.streams.toList

import kotlin.test.assertEquals

class SeatCommandServiceImplTest : IntegralTestSupport() {

    @Autowired
    private lateinit var concertCommandService: ConcertCommandService

    @Autowired
    private lateinit var concertRepository: ConcertRepository

    @Autowired
    private lateinit var seatRepository: SeatRepository

    @Autowired
    private lateinit var seatFacade: SeatFacade

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var queueService: QueueService

    @Autowired
    private lateinit var dbCleaner: DbCleaner

    private var concertId: Long? = null
    private var seatIds: List<Long> = listOf()
    private var savedUser: User? = null

    @BeforeEach
    fun setUp() {
        redisTemplate.connectionFactory?.connection?.flushAll()
        savedUser = userRepository.save(User("nickname", "username", "email@email.com", "password"))
        val request = AddConcertRequest(
            "제목1",
            LocalDate.of(2024, 9, 24),
            LocalDate.of(2024, 9, 25),
            10000,
            20
        )

        concertId = concertCommandService.add(request)
        seatIds = seatRepository.findByConcertId(concertId!!)
            .stream()
            .map(Seat::getId)
            .toList()
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanDatabase()
        redisTemplate.connectionFactory?.connection?.flushAll()
    }

    @DisplayName("모든 남아있는 좌석을 선택하는 1000개의 요청이 들어 올 경우 999개의 요청은 실패한다")
    @Test
    fun selectSeatTest() {
        // given
        val userId = savedUser!!.id!!
        val request = SelectSeatRequest(seatIds)
        queueService.issueTokenAndEnqueue(userId)

        // when
        val threadCount = 1000
        val executorService: ExecutorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val successCount = AtomicInteger()
        val failCount = AtomicInteger()

        for (i in 0 until threadCount) {
            executorService.submit {
                try {
                    seatFacade.selectSeat(request, userId)
                    successCount.incrementAndGet()
                } catch (e: Exception) {
                    failCount.incrementAndGet()
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        // then
        assertEquals(1, successCount.get())
        assertEquals(999, failCount.get())

        executorService.shutdown()
    }
}
