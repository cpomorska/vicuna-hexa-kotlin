package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.util.TESTUSER
import com.scprojekt.util.USER_ID_TESTUSER_1
import com.scprojekt.util.UUID_TESTUSER_1
import com.scprojekt.util.UUID_TESTUSER_2
import com.scprojekt.util.UserTestUtil.Companion.createTestUser
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Consumer

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
class UserRepositoryTest  {

    @Inject
    lateinit var userRepository: UserJpaRepository

    @AfterEach
    @Transactional
    fun teardown() {
        val users: MutableList<User>? = userRepository.findAllToRemove()
        users?.forEach(Consumer { u: User ->
            userRepository.removeEntity(u)
        })
    }

    @Test
    @Transactional
    fun findByUUID() {
        val user: User = createTestUser()
        userRepository.createEntity(user)
        val result: User? = userRepository.findByUUID(UUID_TESTUSER_1)

        assertThat(result?.userNumber!!.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
    }

    @Test
    fun findByType() {
    }

    @Test
    @Transactional
    fun findByName() {
        val user: User = createTestUser()
        userRepository.createEntity(user)
        val result: User = userRepository.findByName(TESTUSER).first()

        assertThat(result.userNumber.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result.userName).isEqualTo(TESTUSER)
    }

    @Test
    fun findByDescription() {
        val user: User = createTestUser()
        userRepository.createEntity(user)
        val result: User = userRepository.findByDescription(TESTUSER).first()

        assertThat(result.userNumber.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result.userDescription).isEqualTo(TESTUSER)
    }

    @Test
    fun findAllInRepository() {
        val user: User = createTestUser()
        userRepository.createEntity(user)
        val result: MutableList<User>? = userRepository.findAllToRemove()

        assertThat(result).isNotEmpty
        assertThat(result?.count()).isEqualTo(USER_ID_TESTUSER_1)
    }

    @Test
    fun findByIdInRepository() {
        val user: User = createTestUser()
        userRepository.createEntity(user)
        val testUser: User? = userRepository.findAllToRemove()?.first()

        val result: User? = testUser?.userNumber?.uuid?.let { userRepository.findByUUID(it.toString()) }
        assertThat(result?.userId).isEqualTo(testUser?.userId)
        assertThat(result?.userNumber?.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result?.userName).isEqualTo(TESTUSER)
    }

    @Test
    @Transactional
    fun createEntity() {
        val newUser: User = createTestUser()
        newUser.userNumber.uuid = UUID.fromString(UUID_TESTUSER_2)

        userRepository.createEntity(newUser)
        val result: User? = userRepository.findByUUID(UUID_TESTUSER_2)

        assertThat(result?.userNumber!!.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_2))
    }

    @Test
    @Transactional
    fun removeEntity() {
        val user: User = createTestUser()
        userRepository.createEntity(user)
        val testUser: User = userRepository.findAllToRemove()?.first()!!
        userRepository.removeEntity(testUser)

        val result: MutableList<User>? = userRepository.findAllToRemove()

        assertThat(result).isEmpty()
        assertThat(result?.count()).isZero()
    }

    @Test
    @Transactional
    fun updateEntity() {
        val newUser: User = createTestUser()
        userRepository.createEntity(newUser)
        val result = userRepository.findByUUID(UUID_TESTUSER_1)

        result?.userNumber?.uuid = UUID.fromString(UUID_TESTUSER_2)
        userRepository.updateEntity(result!!)
        val result1: User? = userRepository.findByUUID(UUID_TESTUSER_2)

        assertThat(result1?.userNumber!!.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_2))
    }

}