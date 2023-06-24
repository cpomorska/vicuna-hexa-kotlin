package com.scprojekt.domain.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.infrastructure.repository.BaseJpaUserRepository
import com.scprojekt.infrastructure.service.BaseUserReadOnlyService
import com.scprojekt.util.TESTUSER
import com.scprojekt.util.UUID_TESTUSER_1
import com.scprojekt.util.UserTestUtil.Companion.createTestUser
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
        val userType = UserType()
        userType.userRoleType = "UserType"

        val result: List<User> = baseUserReadOnlyService.findAllUsersByType(userType)
        assertThat(result[0].userName).isNotEmpty().isEqualTo(TESTUSER)
    }

        @Test
    fun findAllUserByDescription() {
        val result: List<User> = baseUserReadOnlyService.findAllUserByDescription(TESTUSER)
        assertThat(result[0].userDescription).isNotEmpty().isEqualTo(TESTUSER)
    }
}