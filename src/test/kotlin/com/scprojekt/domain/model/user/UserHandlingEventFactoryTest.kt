package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.event.UserEventFactory
import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.model.user.event.UserHandlingEvent
import com.scprojekt.util.UserTestUtil
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@QuarkusTest
class UserHandlingEventFactoryTest {

    @Test
    fun whenCreateUserHandlingEventIsCalledAnEventIsCreated() {
        val user = UserTestUtil.createTestUser()
        val instance = UserEventFactory.getInstance()
        val resultEvent = instance.createUserEvent(UserEventType.CREATE, user)

        assertThat(resultEvent).isInstanceOf(UserHandlingEvent::class.java)
        assertThat(resultEvent).hasFieldOrProperty("userHandlingEventType")
    }

    @Test
    fun whenGetInstanceIsCalledAnInstanceIsCreated() {
        val result = UserEventFactory.getInstance()
        assertThat(result).isInstanceOf(UserEventFactory::class.java)
    }
}