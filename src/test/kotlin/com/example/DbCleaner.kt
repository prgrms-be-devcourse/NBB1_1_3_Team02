package com.example

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DbCleaner(private val jdbcTemplate: JdbcTemplate) {

    fun cleanDatabase() {
        try {
            // 외래 키 제약 조건 비활성화
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE")

            // 각 테이블을 개별적으로 트렁케이트
            jdbcTemplate.execute("TRUNCATE TABLE user")
            jdbcTemplate.execute("TRUNCATE TABLE coupon")
            jdbcTemplate.execute("TRUNCATE TABLE concert")
            jdbcTemplate.execute("TRUNCATE TABLE reservation")
            jdbcTemplate.execute("TRUNCATE TABLE payment")
            jdbcTemplate.execute("TRUNCATE TABLE user_coupon")

            // 외래 키 제약 조건 다시 활성화
            jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE")
        } catch (e: Exception) {
            throw RuntimeException("Failed to clean database", e)
        }
    }
}
