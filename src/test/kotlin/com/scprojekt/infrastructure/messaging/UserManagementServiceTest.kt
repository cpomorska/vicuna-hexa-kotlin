package com.scprojekt.infrastructure.messaging

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.infrastructure.service.UserManagementService
import com.scprojekt.lifecycle.MessagingTestResourcelifecycleManager
import com.scprojekt.util.UserTestUtil
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@QuarkusTest
@QuarkusTestResource(MessagingTestResourcelifecycleManager::class)
class UserManagementServiceTest {

    private lateinit var user: User

    @Inject
    @field:Default
    lateinit var userToBackendProducer: UserToBackendProducer

    @BeforeEach
    fun init(){
        user = UserTestUtil.createTestUser()
    }

    @Test
    fun registerNewUser() {
        val userManagementService = UserManagementService(userToBackendProducer)

        val result = userManagementService.registerNewUser(user)
        assertThat(result).isNotNull()
    }

    @Test
    fun manageExistingUser() {
        val userManagementService = UserManagementService(userToBackendProducer)

        val result = userManagementService.manageExistingUser(user)
        assertThat(result).isNotNull()
    }

    @Test
    fun disableExistingUser() {
        val userManagementService = UserManagementService(userToBackendProducer)

        val result = userManagementService.disableExistingUser(user)
        assertThat(result).isNotNull()
    }

    @Test
    fun deleteExistingUser() {
        val userManagementService = UserManagementService(userToBackendProducer)

        val result = userManagementService.deleteExistingUser(user)
        assertThat(result).isNotNull()
    }
}