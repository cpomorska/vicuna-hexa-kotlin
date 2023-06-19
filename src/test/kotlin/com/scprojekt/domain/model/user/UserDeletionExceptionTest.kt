package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.entity.UserNumber
import com.scprojekt.domain.model.user.exception.UserDeletionException
import com.scprojekt.domain.model.user.exception.UserException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*

class UserDeletionExceptionTest {

    @Test
    fun createdUserDeletionExceptionIsNotNull() {
        val userNumber = UserNumber(UUID.randomUUID())
        val userDeletionException = UserDeletionException(Throwable("Message"), userNumber);

        assertThat(userDeletionException).isInstanceOf(UserException::class.java).isInstanceOf(UserDeletionException::class.java)
        assertThat(userDeletionException.e.message).isNotNull;
    }

    @Test
    fun createdUserDeletionExceptionContainsValidMessage() {
        val userNumber = UserNumber(UUID.randomUUID())
        val userDeletionException = UserDeletionException(Throwable("Message"), userNumber);

        assertThat(userDeletionException).isInstanceOf(UserDeletionException::class.java)
        assertThat(userDeletionException.e).isInstanceOf(Throwable::class.java).message().contains("Message");
    }

    @ParameterizedTest
    @MethodSource("uuidsForUUIDInMessage")
    fun createdUserDeletionExceptionMessageContainsUsernumberUUID(uuid: UUID?) {
        val userNumber = UserNumber(uuid)
        val userDeletionException = UserDeletionException(Throwable("Message"), userNumber);

        assertThat(userDeletionException.e.message).isNotNull;
        assertThat(userDeletionException.message).contains("Message").contains(userNumber.uuid.toString())
    }

    companion object {
        @JvmStatic
        fun uuidsForUUIDInMessage() = listOf(
            Arguments.of(UUID.randomUUID()),
            Arguments.of(UUID.randomUUID())
        )
    }
}