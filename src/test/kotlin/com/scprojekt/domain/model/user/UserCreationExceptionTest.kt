package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.exception.UserCreationException
import com.scprojekt.domain.model.user.exception.UserException
import com.scprojekt.infrastructure.persistence.entity.UserNumberEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*

class UserCreationExceptionTest {

    @Test
    fun createdIsNotNull() {
        val userNumberEntity = UserNumberEntity(UUID.randomUUID())
        val userCreationException = UserCreationException(Throwable("Message"), userNumberEntity.uuid.toString())

        assertThat(userCreationException).isInstanceOf(UserException::class.java).isInstanceOf(UserCreationException::class.java)
        assertThat(userCreationException.e.message).isNotNull
    }

    @Test
    fun createduserCreationExceptionContainsValidMessage() {
        val userNumberEntity = UserNumberEntity(UUID.randomUUID())
        val userCreationException = UserCreationException(Throwable("Message"), userNumberEntity.uuid.toString())

        assertThat(userCreationException).isInstanceOf(UserCreationException::class.java)
        assertThat(userCreationException.e).isInstanceOf(Throwable::class.java).message().contains("Message")
    }

    @ParameterizedTest
    @MethodSource("uuidsForUUIDInMessage")
    fun createduserCreationExceptionMessageContainsUsernumberUUID(uuid: UUID) {
        val userNumberEntity = UserNumberEntity(uuid)
        val userCreationException = UserCreationException(Throwable("Message"), userNumberEntity.uuid.toString())

        assertThat(userCreationException.e.message).isNotNull
        assertThat(userCreationException.message).contains("Message").contains(userNumberEntity.uuid.toString())
    }

    companion object {
        @JvmStatic
        fun uuidsForUUIDInMessage() = listOf(
            Arguments.of(UUID.randomUUID()),
            Arguments.of(UUID.randomUUID())
        )
    }
}