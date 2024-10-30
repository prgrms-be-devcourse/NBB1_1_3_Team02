package com.example.bookYourSeat.service.queue

import com.example.bookYourSeat.IntegralTestSupport
import com.example.bookYourSeat.queue.QueueConst.ALREADY_ISSUED_USER
import com.example.bookYourSeat.queue.QueueConst.PROCESSING
import com.example.bookYourSeat.queue.QueueConst.PROCESSING_QUEUE_KEY
import com.example.bookYourSeat.queue.QueueConst.WAITING
import com.example.bookYourSeat.queue.QueueConst.WAITING_QUEUE_KEY
import com.example.bookYourSeat.queue.util.QueueJwtUtil
import com.example.bookYourSeat.queue.controller.dto.QueueResponse
import com.example.bookYourSeat.queue.service.QueueCommandService
import com.example.bookYourSeat.queue.service.QueueQueryService
import com.example.bookYourSeat.queue.service.facade.QueueService
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.repository.UserRepository
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations

import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class QueueServiceTest : IntegralTestSupport() {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var queueService: QueueService

    @Autowired
    private lateinit var queueCommandService: QueueCommandService

    @Autowired
    private lateinit var queueQueryService: QueueQueryService

    @Autowired
    private lateinit var queueJwtUtil: QueueJwtUtil

    private lateinit var zSet: ZSetOperations<String, String>

    private var testUser: User? = null

    @BeforeEach
    fun beforeEach() {
        zSet = redisTemplate.opsForZSet()
        val user = User("nickname", "username", "email@email.com", "password123456789")
        testUser = userRepository.save(user)
    }

    @AfterEach
    fun afterEach() {
        redisTemplate.connectionFactory?.connection?.flushAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("user는 대기열에 중복 등록할 수 없다.")
    fun issueTokenAndDuplicateEnqueueTest() {
        //given 한 번 등록
        queueCommandService.issueTokenAndEnqueue(testUser!!.id)

        //when 한번 더 등록
        val exception = assertThrows<IllegalArgumentException> {
            queueCommandService.issueTokenAndEnqueue(testUser!!.id)
        }

        //then
        assertEquals(ALREADY_ISSUED_USER, exception.message)
    }

    @Test
    @DisplayName("토큰을 발급하고 진행열이 다 차지 않은 경우 진행열에 넣는다.")
    fun issueTokenAndEnqueueProcessingQueueTest() {
        //when
        val tokenResponse = queueService.issueTokenAndEnqueue(testUser!!.id)
        val token = tokenResponse?.token ?: throw IllegalStateException("Token should not be null")

        //then
        val value = "${testUser!!.id}:$token"
        assertNotNull(zSet.score(PROCESSING_QUEUE_KEY, value))
        assertEquals(1L, zSet.zCard(PROCESSING_QUEUE_KEY))
    }

    @Test
    @DisplayName("토큰을 발급하고 진행열이 다 찬 경우 대기열에 넣는다.")
    fun issueTokenAndEnqueueWaitingQueueTest() {
        //given
        for (i in 1000 until 2000) {
            val savedUser = userRepository.save(User("nickname", "username", "email@email.com", "passwordpassowrd"))
            queueCommandService.issueTokenAndEnqueue(savedUser.id)
        }

        //when
        val tokenResponse = queueService.issueTokenAndEnqueue(testUser!!.id)
        val token = tokenResponse?.token ?: throw IllegalStateException("Token should not be null")

        //then
        val value = "${testUser!!.id}:$token"
        assertNotNull(zSet.score(WAITING_QUEUE_KEY, value))
        assertEquals(1L, zSet.zCard(WAITING_QUEUE_KEY))
    }

    @Test
    @DisplayName("대기열을 탈출한다.")
    fun dequeueWaitingQueueTest() {
        //given
        for (i in 1000 until 2100) {
            val savedUser = userRepository.save(User("nickname", "username", "email@email.com", "passwordpassowrd"))
            queueCommandService.issueTokenAndEnqueue(savedUser.id)
        }

        val tokenResponse = queueService.issueTokenAndEnqueue(testUser!!.id)
        val token = tokenResponse?.token ?: throw IllegalStateException("Token should not be null")

        //when
        queueService.dequeueWaitingQueue(testUser!!.id, token)

        //then
        val value = "${testUser!!.id}:$token"
        assertNull(zSet.score(WAITING_QUEUE_KEY, value))
        assertEquals(100L, zSet.zCard(WAITING_QUEUE_KEY))
    }

    @Test
    @DisplayName("현재 나의 대기열 상태를 조회한다.(진행열에 들어온 상황)")
    fun findQueueStatusTest1() {
        //given
        val tokenResponse = queueService.issueTokenAndEnqueue(testUser!!.id)
        val token = tokenResponse?.token ?: throw IllegalStateException("Token should not be null")

        //when
        val queueResponse = queueService.findQueueStatus(testUser!!.id, token)

        //then
        assertEquals(PROCESSING, queueResponse?.status)
        assertEquals(0L, queueResponse?.waitingQueueCount)
    }

    @Test
    @DisplayName("현재 나의 대기열 상태를 조회한다.(대기열에 있는 상황)")
    fun findQueueStatusTest2() {
        //given
        for (i in 1000 until 2100) {
            val savedUser = userRepository.save(User("nickname", "username", "email@email.com", "passwordpassowrd"))
            queueCommandService.issueTokenAndEnqueue(savedUser.id)
        }

        val tokenResponse = queueService.issueTokenAndEnqueue(testUser!!.id)
        val token = tokenResponse?.token ?: throw IllegalStateException("Token should not be null")

        //when

        val queueResponse = queueQueryService.findQueueStatus(testUser!!.id, token)

        //then

        assertEquals(WAITING, queueResponse?.status)

        assertEquals(101L, queueResponse?.waitingQueueCount)

    }

    @Test

    @DisplayName("API 수행 완료 시 진행열에서 탈출한다.")

    fun completeProcessingTokenTest() {

        //given

        for (i in 1000 until 1500) {

            val savedUser = userRepository.save(User("nickname", "username", "email@email.com", "passwordpassowrd"))

            queueCommandService.issueTokenAndEnqueue(savedUser.id)

        }

        //when

        val tokenResponse = queueCommandService.issueTokenAndEnqueue(testUser!!.id)

        val token = tokenResponse ?: throw IllegalStateException("Token should not be null")

        //then

        assertEquals(501L, zSet.zCard(PROCESSING_QUEUE_KEY))

        //when

        queueCommandService.removeTokenInProcessingQueue(testUser!!.id, token)

        //then

        assertEquals(500L, zSet.zCard(PROCESSING_QUEUE_KEY))

    }

    @Test

    @DisplayName("스케줄러 실행 시 대기열 -> 진행열로 변환된다.")

    fun updateWaitingToProcessingTest() {

        //given

        val tokens = mutableListOf<String>()

        for (i in 1000..1500) {

            val savedUser = userRepository.save(User("nickname", "username", "email@email.com", "passwordpassowrd"))

            tokens.add(queueCommandService.issueTokenAndEnqueue(savedUser.id))

        }

        for (i in 1501..2500) {

            val savedUser = userRepository.save(User("nickname", "username", "email@email.com", "passwordpassowrd"))

            queueCommandService.issueTokenAndEnqueue(savedUser.id)

        }


        queueCommandService.issueTokenAndEnqueue(testUser!!.id)

        //when


        for (token in tokens) {


            val tokenUserId = queueJwtUtil.getUserIdByToken(token)!!


            queueCommandService.removeTokenInProcessingQueue(tokenUserId, token)


        }


        queueCommandService.updateWaitingToProcessing()


        //then


        assertEquals(zSet.zCard(PROCESSING_QUEUE_KEY), 1000L)


        assertEquals(zSet.zCard(WAITING_QUEUE_KEY), 1L)


    }


    @Test


    @DisplayName("processing queue에 빈자리 만큼 waiting queue에서 채워준다.")


    fun updateQueueTest() {


        //given


        val tokens = mutableListOf<String>()


        for (i in 1000 until 1010) {


            val savedUser = userRepository.save(User("nickname", "username", "email@email.com", "passwordpassowrd"))


            tokens.add(queueCommandService.issueTokenAndEnqueue(savedUser.id))


        }


        for (i in 1010 until 2100-1) {


            val savedUser = userRepository.save(User("nickname", "username", "email@email.com", "passwordpassowrd"))


            queueCommandService.issueTokenAndEnqueue(savedUser.id)


        }


        val lastUserId = userRepository.save(User("nickname", "username", "email@email.com", "passwordpassowrd")).id!!


        val lastUserToken = queueCommandService.issueTokenAndEnqueue(lastUserId)


        //when


        val queueResponse1 = queueService.findQueueStatus(lastUserId, lastUserToken)


        for (token in tokens) {


            val tokenUserId = queueJwtUtil.getUserIdByToken(token)!!


            queueCommandService.removeTokenInProcessingQueue(tokenUserId, token)


        }


        queueCommandService.updateWaitingToProcessing()


        val queueResponse2 = queueService.findQueueStatus(lastUserId, lastUserToken)


        //then


        assertNotNull(queueResponse1)


        assertEquals(WAITING, queueResponse1.status)


        assertEquals(100L, queueResponse1.waitingQueueCount)


        assertEquals(0L, queueResponse1.estimatedWaitTime)


        assertNotNull(queueResponse2)


        assertEquals(WAITING, queueResponse2.status)


        assertEquals(90L, queueResponse2.waitingQueueCount)


        assertEquals(0L, queueResponse2.estimatedWaitTime)


    }
}