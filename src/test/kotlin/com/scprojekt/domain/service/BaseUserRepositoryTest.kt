package com.scprojekt.domain.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserNumber
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.infrastructure.repository.BaseJpaUserRepository
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Consumer

private const val TESTROLE = "testrole"
private const val TESTUSER = "Testuser"
private const val USER_ID_TESTUSER_1 = 1L
private const val UUID_TESTUSER_1 = "586c2084-d545-4fac-b7d3-2319382df14f"
private const val UUID_TESTUSER_2 = "35fa10da-594a-4601-a7b7-0a707a3c1ce7"

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
class BaseUserRepositoryTest  {

    @Inject
    @field: Default
    lateinit var userRepository: BaseJpaUserRepository

    @AfterEach
    @Transactional
    fun teardown() {
        val users: MutableList<User>? = userRepository.findAllInRepository()
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

        assertThat(result.userId).isEqualTo(USER_ID_TESTUSER_1)
        assertThat(result.userNumber.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result.userDescription).isEqualTo(TESTUSER)
    }

    @Test
    fun findAllInRepository() {
        val user: User = createTestUser()
        userRepository.createEntity(user)
        val result: MutableList<User>? = userRepository.findAllInRepository()

        assertThat(result).isNotEmpty
        assertThat(result?.count()).isEqualTo(USER_ID_TESTUSER_1)
    }

    @Test
    fun findByIdInRepository() {
        val user: User = createTestUser()
        userRepository.createEntity(user)
        val testUser: User? = userRepository.findAllInRepository()?.first()

        val result: User? = testUser?.userId?.let { userRepository.findByIdInRepository(it) }
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
        val testUser: User = userRepository.findAllInRepository()?.first()!!
        userRepository.removeEntity(testUser)

        val result: MutableList<User>? = userRepository.findAllInRepository()

        assertThat(result).isEmpty()
        assertThat(result?.count()).isZero()
    }

    @Test
    @Transactional
    fun updateEntity() {
    }

    private fun createTestUser(): User {
        val user = User()
        val userType = UserType()
        val userTypeList: MutableList<UserType> = ArrayList()
        val userNumber = UserNumber()
        userNumber.uuid = UUID.fromString(UUID_TESTUSER_1)
        userType.userTypeId = USER_ID_TESTUSER_1
        userType.userRoleType = TESTROLE
        userType.userTypeDescription = TESTUSER
        userTypeList.add(userType)
        user.userId = USER_ID_TESTUSER_1
        user.userName = TESTUSER
        user.userDescription = TESTUSER
        user.userNumber = userNumber
        user.userType = "UserType"
        return user
    }
}