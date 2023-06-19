package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserNumber
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.domain.model.user.event.HandlingEventType
import com.scprojekt.domain.model.user.event.UserHandlingEvent
import com.scprojekt.domain.model.user.event.UserHandlingEventFactory
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

private const val TESTROLE = "testrole"
private const val TESTUSER = "Testuser"
private const val USER_ID_TESTUSER_1 = 1L
private const val UUID_TESTUSER_1 = "586c2084-d545-4fac-b7d3-2319382df14f"
private const val UUID_TESTUSER_2 = "35fa10da-594a-4601-a7b7-0a707a3c1ce7"

@QuarkusTest
class UserHandlingEventFactoryTest {

    @Test
    fun whenCreateUserHandlingEventIsCalledAnEventIsCreated() {
        val user = createTestUser()
        val instance = UserHandlingEventFactory.getInstance()
        val resultEvent = instance.createUserHandlingEvent(HandlingEventType.CREATEUSER, user)

        assertThat(resultEvent).isInstanceOf(UserHandlingEvent::class.java)
        assertThat(resultEvent).hasFieldOrProperty("userHandlingEventType")
    }

    @Test
    fun whenGetInstanceIsCalledAnInstanceIsCreated() {
        val result = UserHandlingEventFactory.getInstance()
        assertThat(result).isInstanceOf(UserHandlingEventFactory::class.java)
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