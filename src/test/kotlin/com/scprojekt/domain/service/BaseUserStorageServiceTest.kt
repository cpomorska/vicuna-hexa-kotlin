package com.scprojekt.domain.service

import com.scprojekt.domain.model.user.dto.response.UuidResponse
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.repository.UserJpaRepository
import com.scprojekt.infrastructure.service.UserReadOnlyService
import com.scprojekt.infrastructure.service.UserStorageService
import com.scprojekt.util.TestUtil.Companion.createTestUser
import com.scprojekt.util.UUID_TESTUSER_1
import com.scprojekt.util.TESTUSER
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import io.quarkus.test.common.WithTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.apache.camel.CamelContext
import org.apache.camel.impl.engine.DefaultManagementStrategy
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Consumer

@QuarkusTest
@WithTestResource(H2DatabaseTestResource::class)
class BaseUserStorageServiceTest : CamelQuarkusTestSupport() {

    private lateinit var baseUserStorageService: UserStorageService
    private lateinit var baseUserReadOnlyService : UserReadOnlyService


    @Inject
    lateinit var camelContext: CamelContext

    @Inject
    lateinit var userRepository: UserJpaRepository


    @BeforeEach
    fun init(){
        baseUserReadOnlyService = UserReadOnlyService(userRepository)
        baseUserStorageService = UserStorageService(userRepository)
//        camelContext.managementStrategy = DefaultManagementStrategy()
    }

    @AfterEach
    @Transactional
    fun teardown() {
        userRepository.deleteByUsername(TESTUSER)
        userRepository.deleteByUsername("Nanana")
    }

    @Test
    @Transactional
    fun whenCreateUserIsCalledTheUserIsCreated() {
        val testUuid = UUID.fromString(UUID_TESTUSER_1)
        val result: UuidResponse = this.baseUserStorageService.createUser(createTestUser())
        assertThat(result).isNotNull().isInstanceOf(UuidResponse::class.java)
        assertThat(result.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertEquals(testUuid, result.uuid)
    }

    @Test
    @Transactional
    fun whenUpdateUserIsCalledTheUserIsUpdated() {
        val resultUUID1 = baseUserStorageService.createUser(createTestUser())
        val result1: UserEntity = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)!!
        assertThat(result1.userNumber.uuid).isEqualTo(resultUUID1.uuid)

        result1.userName = "Nanana"
        baseUserStorageService.updateUser(result1)
        val result: List<UserEntity> = baseUserReadOnlyService.findAllUserByName("Nanana")

        assertThat(result).isNotEmpty
        assertThat(result.first().userName).isEqualTo("Nanana")
    }

    @Test
    @Transactional
    fun whenRemoveUserIsCalledTheUserIsRemoved() {
        baseUserStorageService.createUser(createTestUser())
        val result1 = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)

        val uuidResult = baseUserStorageService.removeUser(result1!!)
        val result: UserEntity? = baseUserReadOnlyService.getUserByUuid(UUID_TESTUSER_1)

        assertThat(uuidResult.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result).isNull()
    }
}
