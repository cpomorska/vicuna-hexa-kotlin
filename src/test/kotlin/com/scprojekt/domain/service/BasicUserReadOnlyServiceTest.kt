package com.scprojekt.domain.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.infrastructure.repository.UserJpaRepository
import com.scprojekt.infrastructure.service.UserReadOnlyService
import com.scprojekt.util.TESTUSER
import com.scprojekt.util.TestUtil.Companion.createTestUser
import com.scprojekt.util.UUID_TESTUSER_1
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Consumer

@Disabled
@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
class BasicUserReadOnlyServiceTest {

    @Inject
    @field: Default
    lateinit var userRepository: UserJpaRepository

    @Inject
    @field:Default
    lateinit var baseUserReadOnlyService : UserReadOnlyService

    @BeforeEach
    fun init(){
        userRepository.createEntity(createTestUser())
    }

    @AfterEach
    @Transactional
    fun teardown() {
        val users: MutableList<User>? = userRepository.findAllToRemove()
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
        var user = createTestUser()

        val result: List<User> = baseUserReadOnlyService.findAllUsersByType(user.userType)
        assertThat(result.first().userName).isNotEmpty().isEqualTo(TESTUSER)
    }

    @Test
    fun findAllUserByDescription() {
        val result: List<User> = baseUserReadOnlyService.findAllUserByDescription(TESTUSER)
        assertThat(result[0].userDescription).isNotEmpty().isEqualTo(TESTUSER)
    }
}