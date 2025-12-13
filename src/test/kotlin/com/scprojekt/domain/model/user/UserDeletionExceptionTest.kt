package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.exception.UserDeletionException
import com.scprojekt.domain.model.user.exception.UserException
import com.scprojekt.infrastructure.persistence.entity.UserNumberEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*

class UserDeletionExceptionTest {

    @Test
    fun createdUserDeletionExceptionIsNotNull() {
        val userNumberEntity = UserNumberEntity(UUID.randomUUID())
        val userDeletionException = UserDeletionException(Throwable("Message"), userNumberEntity.uuid.toString())

        assertThat(userDeletionException).isInstanceOf(UserException::class.java).isInstanceOf(UserDeletionException::class.java)
        assertThat(userDeletionException.e.message).isNotNull
    }

    @Test
    fun createdUserDeletionExceptionContainsValidMessage() {
        val userNumberEntity = UserNumberEntity(UUID.randomUUID())
        val userDeletionException = UserDeletionException(Throwable("Message"), userNumberEntity.uuid.toString())

        assertThat(userDeletionException).isInstanceOf(UserDeletionException::class.java)
        assertThat(userDeletionException.e).isInstanceOf(Throwable::class.java).message().contains("Message")
    }

    @ParameterizedTest
    @MethodSource("uuidsForUUIDInMessage")
    fun createdUserDeletionExceptionMessageContainsUsernumberUUID(uuid: UUID) {
        val userNumberEntity = UserNumberEntity(uuid)
        val userDeletionException = UserDeletionException(Throwable("Message"), userNumberEntity.uuid.toString());

        assertThat(userDeletionException.e.message).isNotNull
        assertThat(userDeletionException.message).contains("Message").contains(userNumberEntity.uuid.toString())
    }

    companion object {
        @JvmStatic
        fun uuidsForUUIDInMessage() = listOf(
            Arguments.of(UUID.randomUUID()),
            Arguments.of(UUID.randomUUID())
        )
    }
}