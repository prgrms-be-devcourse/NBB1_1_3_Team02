package com.example.bookYourSeat.service.concert

import com.example.book_your_seat.IntegralTestSupport
import com.example.book_your_seat.concert.controller.dto.AddConcertRequest
import com.example.book_your_seat.concert.controller.dto.ConcertResponse
import com.example.book_your_seat.concert.service.ConcertCommandService
import com.example.book_your_seat.concert.service.ConcertQueryService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalDateTime

class ConcertServiceTest : IntegralTestSupport() {

    @Autowired
    private lateinit var concertCommandService: ConcertCommandService

    @Autowired
    private lateinit var concertQueryService: ConcertQueryService

    @DisplayName("제목, 시작 시간, 종료 시간, 금액, 러닝 타임을 입력하면 Concert 를 생성한다.")
    @Test
    fun addTest() {
        // given
        val request = AddConcertRequest(
            "제목1",
            LocalDate.of(2024, 9, 24),
            LocalDate.of(2024, 9, 25),
            10000,
            120
        )

        // when
        val id = concertCommandService.add(request)
        val response = concertQueryService.findById(id)

        // then  쿼리 101개 전송.. Concert 1, Seat 100..
        assertThat(response)
            .extracting("title", "startDate", "endDate", "price", "startHour")
            .containsExactly(
                "제목1",
                LocalDate.of(2024, 9, 24),
                LocalDate.of(2024, 9, 25),
                10000,
                120
            )
    }

    @DisplayName("Concert 를 등록하면 예매 가능 날짜는 공연 시작일로부터 일주일 전, 낮 12시로 설정된다.")
    @Test
    fun reservationTimeTest() {
        // given
        val request = AddConcertRequest(
            "제목1",
            LocalDate.of(2024, 9, 24),
            LocalDate.of(2024, 9, 25),
            10000,
            14
        )
        val id = concertCommandService.add(request)

        // when
        val response = concertQueryService.findById(id)

        assertThat(response.reservationStartAt)
            .isEqualTo(LocalDateTime.of(2024, 9, 17, 12, 0, 0))
    }

    @DisplayName("Concert 전체 목록을 조회한다.")
    @Test
    fun findAllTest() {
        // given
        val request1 = AddConcertRequest(
            "제목1",
            LocalDate.of(2024, 9, 24),
            LocalDate.of(2024, 9, 25),
            10000,
            120
        )
        val request2 = AddConcertRequest(
            "제목2",
            LocalDate.of(2024, 10, 24),
            LocalDate.of(2024, 11, 25),
            90000,
            999
        )
        concertCommandService.add(request1)
        concertCommandService.add(request2)

        // when
        val responses = concertQueryService.findAll()

        // then
        assertThat(responses)
            .extracting(ConcertResponse::title)
            .contains("제목1", "제목2")
    }

    @DisplayName("Concert 의 Id 를 전송하면 해당 Concert 의 정보를 조회한다")
    @Test
    fun findByIdTest() {
        // given
        val request = AddConcertRequest(
            "제목1",
            LocalDate.of(2024, 9, 24),
            LocalDate.of(2024, 9, 25),
            10000,
            120
        )
        val id = concertCommandService.add(request)

        // when
        val result = concertQueryService.findById(id)

        // then
        assertThat(result)
            .extracting("title", "startDate", "endDate", "price", "startHour")
            .containsExactly(
                "제목1",
                LocalDate.of(2024, 9, 24),
                LocalDate.of(2024, 9, 25),
                10000,
                120
            )
    }

    @DisplayName("Concert 의 Id 를 입력받아서 해당 Concert 를 삭제한다.")
    @Test
    fun deleteTest() {
        // given
        val request = AddConcertRequest(
            "제목1",
            LocalDate.of(2024, 9, 24),
            LocalDate.of(2024, 9, 25),
            10000,
            120
        )
        val id = concertCommandService.add(request)

        // when
        concertCommandService.delete(id)

        // then
        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { concertQueryService.findById(id) }
    }
}
