package com.example.bookYourSeat

import com.example.DbCleaner
import com.example.bookYourSeat.config.JpaAuditingConfig
import com.example.bookYourSeat.payment.controller.TossApiService
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import

@Import(DbCleaner::class)
@SpringBootTest
@MockBean(JpaAuditingConfig::class)
abstract class IntegralTestSupport {

    @MockBean
    protected lateinit var tossApiService: TossApiService
}
