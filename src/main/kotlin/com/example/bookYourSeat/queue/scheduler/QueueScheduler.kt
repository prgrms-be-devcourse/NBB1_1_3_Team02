package com.example.bookYourSeat.queue.scheduler

import com.example.bookYourSeat.queue.service.QueueCommandService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class QueueScheduler(
    private val queueCommandService: QueueCommandService
) {
    @Scheduled(fixedRate = 30 * 1000) //30초마다
    fun updateWaitingToProcessing() {
        queueCommandService.removeExpiredToken()
        queueCommandService.updateWaitingToProcessing()
//        log.info("processing queue 가 update 되었습니다.")
    }
}
