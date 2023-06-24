package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.event.HandlingEventType
import com.scprojekt.domain.model.user.event.UserHandlingEvent
import com.scprojekt.domain.model.user.event.UserHandlingEventFactory
import com.scprojekt.util.UserTestUtil
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@QuarkusTest
class UserHandlingEventFactoryTest {

    @Test
    fun whenCreateUserHandlingEventIsCalledAnEventIsCreated() {
        val user = UserTestUtil.createTestUser()
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
}