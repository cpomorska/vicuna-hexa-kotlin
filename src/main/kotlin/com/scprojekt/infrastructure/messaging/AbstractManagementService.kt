package com.scprojekt.infrastructure.messaging

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.event.UserEventFactory
import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.model.user.exception.UserException
import io.vertx.core.impl.logging.Logger
import io.vertx.core.impl.logging.LoggerFactory
import lombok.extern.slf4j.Slf4j
import java.util.*

@Slf4j
abstract class AbstractManagementService(private val userToBackendProducer: UserToBackendProducer) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    protected fun processUser(user: User, userEvent: UserEventType): UUID {
        try {
            val userHandlingEvent = UserEventFactory.getInstance()
                .createUserEvent(userEvent, user)
            userToBackendProducer.produceUserHandlingEvent(userHandlingEvent)
        } catch (uex: Exception) {
            if (uex is UserException) {logger.info(uex)} else throw uex

        }
        return user.userNumber.uuid!!
    }
}