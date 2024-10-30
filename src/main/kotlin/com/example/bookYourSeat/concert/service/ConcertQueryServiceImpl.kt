package com.example.bookYourSeat.concert.service

import com.example.book_your_seat.concert_kotlin.ConcertConst
import com.example.bookYourSeat.concert.controller.dto.ConcertListResponse
import com.example.bookYourSeat.concert.controller.dto.ConcertResponse
import com.example.book_your_seat.concert_kotlin.controller.dto.ResultRedisConcert
import com.example.bookYourSeat.concert.repository.ConcertRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
open class ConcertQueryServiceImpl(
    private val concertRepository: ConcertRepository
) : ConcertQueryService {

    override fun findAll(): List<ConcertResponse> {
        return concertRepository.findAll().map { concert -> ConcertResponse.from(concert!!) }
    }

    override fun findById(id: Long): ConcertResponse {
        val concert = concertRepository.findById(id)
            .orElseThrow { IllegalArgumentException("${ConcertConst.INVALID_CONCERT_ID} $id") }
        return ConcertResponse.from(concert!!)
    }

    override fun findAllConcertList(): List<ConcertListResponse> {
        return concertRepository.findAll().map { ConcertListResponse.from(it!!) }
    }

    @Cacheable(cacheNames = ["concerts"], key = "'allConcerts'")
    override fun findUsedRedisAllConcertList(): ResultRedisConcert {
        val list = concertRepository.findAll().map { ConcertListResponse.from(it!!) }
        return ResultRedisConcert(list)
    }
}