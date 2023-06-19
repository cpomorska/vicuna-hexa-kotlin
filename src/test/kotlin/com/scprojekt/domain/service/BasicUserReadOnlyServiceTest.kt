package com.scprojekt.domain.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserNumber
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.infrastructure.repository.BaseJpaUserRepository
import com.scprojekt.infrastructure.service.BaseUserReadOnlyService
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Consumer

private const val TESTROLE = "testrole"
private const val TESTUSER = "Testuser"
private const val USER_ID_TESTUSER_1 = 1L
private const val UUID_TESTUSER_1 = "586c2084-d545-4fac-b7d3-2319382df14f"

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
class BasicUserReadOnlyServiceTest {

    @Inject
    @field: Default
    lateinit var userRepository: BaseJpaUserRepository

    @Inject
    @field:Default
    lateinit var baseUserReadOnlyService : BaseUserReadOnlyService

    @BeforeEach
    fun init(){
        userRepository.createEntity(createTestUser())
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
    fun testGetUser() {
        val result: User? = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)
        assertThat(result?.userNumber!!.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
    }

    @Test
    fun findAllUserByName() {
        val result: List<User> = baseUserReadOnlyService.findAllUserByName(TESTUSER)
        assertThat(result[0].userName).isNotEmpty().isEqualTo(TESTUSER)
    }

    @Test
    fun findAllUserByType() {
        val userType: UserType = UserType()
        userType.userRoleType = "UserType"

        val result: List<User> = baseUserReadOnlyService.findAllUsersByType(userType)
        assertThat(result[0].userName).isNotEmpty().isEqualTo(TESTUSER)
    }

        @Test
    fun findAllUserByDescription() {
        val result: List<User> = baseUserReadOnlyService.findAllUserByDescription(TESTUSER)
        assertThat(result[0].userDescription).isNotEmpty().isEqualTo(TESTUSER)
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