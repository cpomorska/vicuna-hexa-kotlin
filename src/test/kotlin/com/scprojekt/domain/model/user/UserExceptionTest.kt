package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.exception.UserException
import com.scprojekt.infrastructure.persistence.entity.UserNumberEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*

class UserExceptionTest {

    @Test
    fun createdUserExceptionIsNotNull() {
        val userNumberEntity = UserNumberEntity(UUID.randomUUID())
        val userException = UserException(Throwable("Message"), userNumberEntity.uuid.toString())

        assertThat(userException.e.message).isNotNull
    }

    @ParameterizedTest
    @MethodSource("uuidsForUUIDInMessage")
    fun createdUserExceptionMessageContainsUsernumberUUID(uuid: UUID) {
        val userNumberEntity = UserNumberEntity(uuid)
        val userException = UserException(Throwable("Message"), userNumberEntity.uuid.toString())

        assertThat(userException.e.message).isNotNull
        assertThat(userException.message).contains("Message").contains(userNumberEntity.uuid.toString())
    }

    companion object {
        @JvmStatic
        fun uuidsForUUIDInMessage() = listOf(
            Arguments.of(UUID.randomUUID()),
            Arguments.of(UUID.randomUUID())
        )
    }
}