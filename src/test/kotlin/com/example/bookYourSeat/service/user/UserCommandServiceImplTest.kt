package com.example.bookYourSeat.service.user

import com.example.DbCleaner
import com.example.bookYourSeat.IntegralTestSupport
import com.example.bookYourSeat.config.security.jwt.SecurityJwtUtil
import com.example.bookYourSeat.user.controller.dto.*
import com.example.bookYourSeat.user.domain.Address
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.mail.repository.MailRedisRepository
import com.example.bookYourSeat.user.mail.service.MailService
import com.example.bookYourSeat.user.repository.AddressRepository
import com.example.bookYourSeat.user.repository.UserRepository
import com.example.bookYourSeat.user.service.command.UserCommandService
import com.example.bookYourSeat.user.service.facade.UserFacade
import com.example.bookYourSeat.user.service.query.UserQueryService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.transaction.annotation.Transactional
import kotlin.test.DefaultAsserter.assertEquals

class UserCommandServiceImplTest : IntegralTestSupport() {

    @Autowired
    private lateinit var dbCleaner: DbCleaner

    @Autowired
    private lateinit var userCommandService: UserCommandService

    @Autowired
    private lateinit var userQueryService: UserQueryService

    @Autowired
    private lateinit var userFacade: UserFacade

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var mailRedisRepository: MailRedisRepository

    @Autowired
    private lateinit var mailService: MailService

    @Autowired
    private lateinit var securityJwtUtil: SecurityJwtUtil

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    private lateinit var existingUser: User

    @BeforeEach
    fun setUp() {
        existingUser = User("nickname", "username", "test@test.com", "passwordpassword")
        userRepository.save(existingUser)
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanDatabase()
        redisTemplate.connectionFactory?.connection?.flushAll()
    }

    @Test
    @DisplayName("회원 가입에 성공한다.")
    fun joinWithValidDataTest() {
        // given
        mailRedisRepository.saveVerifiedEmail("test2@test.com")
        val joinRequest = JoinRequest("nickname", "username", "test2@test.com", "passwordpassword")

        // when
        val response = userFacade.join(joinRequest)

        // then
        assertThat(response).isNotNull()
        assertThat(userRepository.findById(response.userId)).isPresent()
    }

    @Test
    @DisplayName("인증되지 않은 이메일로 회원가입 할 경우, 예외가 발생한다.")
    fun joinEmailNotVerifiedTest() {
        // given
        val joinRequest = JoinRequest("nickname", "username", "test2@test.com", "passwordpassword")

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            userFacade.join(joinRequest)
        }
        assertEquals(EMAIL_NOT_VERIFIED, exception.message)
    }

    @Test
    @DisplayName("중복되는 이메일로는 가입 할 수 없다.")
    fun joinWithDuplicateEmailTest() {
        // given
        val mail = "test@test.com"

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            userFacade.sendCertMail(mail)
        }
        assertEquals(ALREADY_JOIN_EMAIL, exception.message)
    }

    @Test
    @DisplayName("인증 코드가 올바르지 않으면 예외가 발생한다.")
    fun checkCertCodeTest() {
        // given
        val mail = "test2@test.com"
        val certCode = "C12345"
        val certCode2 = "AABBC"
        mailRedisRepository.saveEmailCertCode(mail, certCode)

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            mailService.checkCertCode(mail, certCode2)
        }
        assertEquals(INVALID_CERT_CODE, exception.message)
    }

    @Test
    @DisplayName("로그인에 성공한다.")
    fun loginWithValidDataTest() {
        // given
        val joinRequest = JoinRequest("nickname", "username", "test2@test.com", "passwordpassword")
        val userId = userCommandService.join(joinRequest).userId()
        val loginRequest = LoginRequest("test2@test.com", "passwordpassword")

        // when
        val response = userCommandService.login(loginRequest)
        val email = securityJwtUtil.getEmailByToken(response.accessToken)

        // then
        val testUser = userRepository.findByEmail(email).get()
        assertThat(userId).isEqualTo(testUser.id)
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 하면 실패한다.")
    fun loginWithInvalidCredentialsTest() {
        // given
        val loginRequest = LoginRequest("test@test.com", "wrongpassword")

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            userCommandService.login(loginRequest)
        }
        assertEquals(INVALID_LOGIN_REQUEST, exception.message)
    }

    @Test
    @Transactional
    @DisplayName("주소를 추가한다.")
    fun addAddressTest() {
        // given
        val addAddressRequest = AddAddressRequest("postcode", "detail")

        // when
        val addressIdResponse = userFacade.addAddress(existingUser.id, addAddressRequest)
        val list = existingUser.addressList

        // then
        assertThat(list.size).isEqualTo(1)
        assertThat(list[0].user.id).isEqualTo(existingUser.id)
        assertThat(list[0].id).isEqualTo(addressIdResponse.addressId)
    }

    @Test
    @DisplayName("주소를 삭제한다.")
    fun deleteAddressTest() {
        // given
        val addAddressRequest = AddAddressRequest("postcode", "detail")
        val addressIdResponse = userFacade.addAddress(existingUser.id, addAddressRequest)

        // when
        userFacade.deleteAddress(existingUser.id, addressIdResponse.addressId)
        val byId = addressRepository.findById(addressIdResponse.addressId)

        // then
        assertThat(byId.isEmpty).isTrue()
    }

    @Test
    @DisplayName("본인의 주소가 아니면 삭제할 수 없다.")
    fun deleteAddressFailTest() {
        // given
        val newUser = User("nickname", "username", "test@test.com", "passwordpassword")
        userRepository.saveAndFlush(newUser)

        val addAddressRequest = AddAddressRequest("postcode", "detail")
        val addressIdResponse = userFacade.addAddress(newUser.id, addAddressRequest)

        // when & then
        val exception = assertThrows<IllegalArgumentException> {
            userFacade.deleteAddress(existingUser.id, addressIdResponse.addressId)
        }
        assertEquals(ADDRESS_NOT_OWNED, exception.message)
    }

    @Test
    @Transactional
    @DisplayName("유저의 주소 목록을 반환한다.")
    fun getUserAddressListTest() {
        // given
        val addAddressRequest1 = AddAddressRequest("postcode", "detail")
        userFacade.addAddress(existingUser.id, addAddressRequest1)
        val addAddressRequest2 = AddAddressRequest("postcode2", "detail2")
        userFacade.addAddress(existingUser.id, addAddressRequest2)

        // when
        val list = userQueryService.getUserAddressList(existingUser.id)

        // then
        assertEquals(2, list.size)
        assertThat(list.map { it.postcode }).containsExactly("postcode", "postcode2")
    }
}
