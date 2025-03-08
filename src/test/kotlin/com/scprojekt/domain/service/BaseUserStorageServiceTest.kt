package com.scprojekt.domain.service

import com.scprojekt.domain.model.user.dto.response.UuidResponse
import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.infrastructure.repository.UserJpaRepository
import com.scprojekt.infrastructure.service.UserReadOnlyService
import com.scprojekt.infrastructure.service.UserStorageService
import com.scprojekt.util.TestUtil.Companion.createTestUser
import com.scprojekt.util.UUID_TESTUSER_1
import io.quarkus.test.common.WithTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Consumer

@Disabled
@WithTestResource(H2DatabaseTestResource::class)
class BaseUserStorageServiceTest {

    private lateinit var baseUserStorageService: UserStorageService
    private lateinit var baseUserReadOnlyService : UserReadOnlyService

    @Inject
    lateinit var userRepository: UserJpaRepository


    @BeforeEach
    fun init(){
        baseUserReadOnlyService = UserReadOnlyService(userRepository)
        baseUserStorageService = UserStorageService(userRepository)
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
    @Transactional
    fun whenCreateUserIsCalledTheUserIsCreated() {
        val testUuid = UUID.fromString(UUID_TESTUSER_1)
        val result: UuidResponse = createTestUser().let { baseUserStorageService.createUser(it) }
        assertThat(result).isNotNull().isInstanceOf(UuidResponse::class.java)
        assertThat(result.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertEquals(testUuid, result.uuid)
    }

    @Test
    @Transactional
    fun whenUpdateUserIsCalledTheUserIsUpdated() {
        val resultUUID1 = createTestUser().let { baseUserStorageService.createUser(it) }
        val result1: User = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)!!
        assertThat(result1.userNumber.uuid).isEqualTo(resultUUID1.uuid)

        result1.userName = "Nanana"
        baseUserStorageService.updateUser(result1)
        val result: List<User> = baseUserReadOnlyService.findAllUserByName("Nanana")

        assertThat(result).isNotEmpty
        assertThat(result.first().userName).isEqualTo("Nanana")
    }

    @Test
    @Transactional
    fun whenRemoveUserIsCalledTheUserIsRemoved() {
        createTestUser().let { baseUserStorageService.createUser(it) }
        val result1: User = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)!!

        val uuidResult = result1.let { baseUserStorageService.removeUser(it) }
        val result: User? = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)

        assertThat(uuidResult.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result).isNull()
    }
}
