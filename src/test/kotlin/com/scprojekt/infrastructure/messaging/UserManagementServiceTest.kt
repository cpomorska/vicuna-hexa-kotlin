package com.scprojekt.infrastructure.messaging

import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.service.UserManagementService
import com.scprojekt.lifecycle.MessagingTestResourcelifecycleManager
import com.scprojekt.util.TestUtil.Companion.createTestUser
import io.quarkus.test.common.WithTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@QuarkusTest
@WithTestResource(MessagingTestResourcelifecycleManager::class)
class UserManagementServiceTest {

    private lateinit var userEntity: UserEntity

    @Inject
    @field:Default
    lateinit var userToBackendProducer: UserToBackendProducer

    @BeforeEach
    fun init(){
        userEntity = createTestUser()
    }

    @Test
    fun registerNewUser() {
        val userManagementService = UserManagementService(userToBackendProducer)

        val result = userManagementService.registerNewUser(userEntity)
        assertThat(result).isNotNull()
    }

    @Test
    fun manageExistingUser() {
        val userManagementService = UserManagementService(userToBackendProducer)

        val result = userManagementService.manageExistingUser(userEntity)
        assertThat(result).isNotNull()
    }

    @Test
    fun disableExistingUser() {
        val userManagementService = UserManagementService(userToBackendProducer)

        val result = userManagementService.disableExistingUser(userEntity)
        assertThat(result).isNotNull()
    }

    @Test
    fun deleteExistingUser() {
        val userManagementService = UserManagementService(userToBackendProducer)

        val result = userManagementService.deleteExistingUser(userEntity)
        assertThat(result).isNotNull()
    }
}