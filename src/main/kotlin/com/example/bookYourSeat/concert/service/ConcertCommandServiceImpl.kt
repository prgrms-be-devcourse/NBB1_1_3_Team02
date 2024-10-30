package com.example.book_your_seat.concert_kotlin.service

import com.example.book_your_seat.concert_kotlin.ConcertConst
import com.example.book_your_seat.concert_kotlin.controller.dto.AddConcertRequest
import com.example.book_your_seat.concert_kotlin.controller.dto.toConcert

import com.example.book_your_seat.concert_kotlin.domain.Concert
import com.example.book_your_seat.concert_kotlin.repository.ConcertRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class ConcertCommandServiceImpl(
    private val concertRepository: ConcertRepository
) : ConcertCommandService {

    private val logger = LoggerFactory.getLogger(ConcertCommandServiceImpl::class.java)

    override fun add(request: AddConcertRequest): Long {
        return try {
            val concert: Concert = request.toConcert()
            concertRepository.saveBulkData(concert)
        } catch (ex: Exception) {
            logger.error("Failed to add concert: ${request.title}", ex)
            throw ex
        }
    }


    override fun delete(id: Long) {
        try {
            val concert = concertRepository.findById(id)
                .orElseThrow { IllegalArgumentException("${ConcertConst.INVALID_CONCERT_ID} $id") }
            concertRepository.deleteBulkData(concert!!.id)
        } catch (ex: Exception) {
            logger.error("Failed to delete concert with ID: $id", ex)
            throw ex
        }
    }
}