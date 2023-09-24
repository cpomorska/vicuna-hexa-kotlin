package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.entity.UserNumber
import com.scprojekt.domain.model.user.exception.UserCreationException
import com.scprojekt.domain.model.user.exception.UserException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*

class Test {

    @Test
    fun createdIsNotNull() {
        val userNumber = UserNumber(UUID.randomUUID())
        val userCreationException = UserCreationException(Throwable("Message"), userNumber.uuid.toString())

        assertThat(userCreationException).isInstanceOf(UserException::class.java).isInstanceOf(UserCreationException::class.java)
        assertThat(userCreationException.e.message).isNotNull
    }

    @Test
    fun createduserCreationExceptionContainsValidMessage() {
        val userNumber = UserNumber(UUID.randomUUID())
        val userCreationException = UserCreationException(Throwable("Message"), userNumber.uuid.toString())

        assertThat(userCreationException).isInstanceOf(UserCreationException::class.java)
        assertThat(userCreationException.e).isInstanceOf(Throwable::class.java).message().contains("Message")
    }

    @ParameterizedTest
    @MethodSource("uuidsForUUIDInMessage")
    fun createduserCreationExceptionMessageContainsUsernumberUUID(uuid: UUID?) {
        val userNumber = UserNumber(uuid)
        val userCreationException = UserCreationException(Throwable("Message"), userNumber.uuid.toString())

        assertThat(userCreationException.e.message).isNotNull
        assertThat(userCreationException.message).contains("Message").contains(userNumber.uuid.toString())
    }

    companion object {
        @JvmStatic
        fun uuidsForUUIDInMessage() = listOf(
            Arguments.of(UUID.randomUUID()),
            Arguments.of(UUID.randomUUID())
        )
    }
}