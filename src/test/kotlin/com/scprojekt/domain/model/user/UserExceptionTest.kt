package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.entity.UserNumber
import com.scprojekt.domain.model.user.exception.UserException
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*

@QuarkusTest
class UserExceptionTest {

    @Test
    fun createdUserExceptionIsNotNull() {
        val userNumber = UserNumber(UUID.randomUUID())
        val userException = UserException(Throwable("Message"), userNumber.uuid.toString())

        assertThat(userException.e.message).isNotNull
    }

    @ParameterizedTest
    @MethodSource("uuidsForUUIDInMessage")
    fun createdUserExceptionMessageContainsUsernumberUUID(uuid: UUID?) {
        val userNumber = UserNumber(uuid)
        val userException = UserException(Throwable("Message"), userNumber.uuid.toString())

        assertThat(userException.e.message).isNotNull
        assertThat(userException.message).contains("Message").contains(userNumber.uuid.toString())
    }

    companion object {
        @JvmStatic
        fun uuidsForUUIDInMessage() = listOf(
            Arguments.of(UUID.randomUUID()),
            Arguments.of(UUID.randomUUID())
        )
    }
}