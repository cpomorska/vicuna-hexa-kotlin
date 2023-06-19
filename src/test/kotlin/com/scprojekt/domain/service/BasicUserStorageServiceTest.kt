package com.scprojekt.domain.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserNumber
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.infrastructure.repository.BaseJpaUserRepository
import com.scprojekt.infrastructure.service.BaseUserReadOnlyService
import com.scprojekt.infrastructure.service.BaseUserStorageService
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
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
class BaseUserStorageServiceTest {

    private lateinit var baseUserStorageService: BaseUserStorageService
    private lateinit var baseUserReadOnlyService : BaseUserReadOnlyService

    @Inject
    @field: Default
    lateinit var userRepository: BaseJpaUserRepository


    @BeforeEach
    fun init(){
        baseUserReadOnlyService = BaseUserReadOnlyService(userRepository)
        baseUserStorageService = BaseUserStorageService(userRepository)
    }

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
    fun whenCreateUserIsCalledTheUserIsCreated() {
        val testUuid = UUID.fromString(UUID_TESTUSER_1)
        val result: UUID = createTestUser().let { baseUserStorageService.createUser(it) }
        assertThat(result).isNotNull().isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertEquals(testUuid, result)
    }

    @Test
    @Transactional
    fun whenUpdateUserIsCalledTheUserIsUpdated() {
        val uuidUpdated = UUID.fromString(UUID_TESTUSER_2)

        val resultUUID1 = createTestUser().let { baseUserStorageService.createUser(it) }
        var result1: User = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)!!
        assertThat(result1.userNumber.uuid).isEqualTo(resultUUID1)

        result1.userNumber.uuid = uuidUpdated
        baseUserStorageService.updateUser(result1)
        val result: User = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)!!

        assertThat(result).isNotNull
        assertThat(result.userNumber.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_2))
    }

    @Test
    @Transactional
    fun whenRemoveUserIsCalledTheUserIsRemoved() {
        createTestUser().let { baseUserStorageService.createUser(it) }
        var result1: User = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)!!

        val uuidResult = result1.let { baseUserStorageService.removeUser(it) }
        val result: User? = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)

        assertThat(uuidResult).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result).isNull()
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