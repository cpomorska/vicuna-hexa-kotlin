package com.scprojekt.domain.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.infrastructure.repository.UserJpaRepository
import com.scprojekt.infrastructure.service.UserReadOnlyService
import com.scprojekt.infrastructure.service.UserStorageService
import com.scprojekt.util.UUID_TESTUSER_1
import com.scprojekt.util.UUID_TESTUSER_2
import com.scprojekt.util.UserTestUtil.Companion.createTestUser
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


@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
class BaseUserStorageServiceTest {

    private lateinit var baseUserStorageService: UserStorageService
    private lateinit var baseUserReadOnlyService : UserReadOnlyService

    @Inject
    @field: Default
    lateinit var userRepository: UserJpaRepository


    @BeforeEach
    fun init(){
        baseUserReadOnlyService = UserReadOnlyService(userRepository)
        baseUserStorageService = UserStorageService(userRepository)
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
        val result1: User = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)!!
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
        val result1: User = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)!!

        val uuidResult = result1.let { baseUserStorageService.removeUser(it) }
        val result: User? = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)

        assertThat(uuidResult).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result).isNull()
    }
}