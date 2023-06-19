package com.scprojekt.infrastructure.messaging

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.event.HandlingEventType
import com.scprojekt.domain.model.user.event.UserHandlingEventFactory
import com.scprojekt.domain.model.user.exception.UserException
import io.vertx.core.impl.logging.Logger
import io.vertx.core.impl.logging.LoggerFactory
import lombok.extern.slf4j.Slf4j
import java.util.*

@Slf4j
abstract class AbstractManagementService(userToBackendProducer: UserToBackendProducer) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private val userToBackendProducer : UserToBackendProducer = userToBackendProducer

    protected fun processUser(user: User, handlingEvent: HandlingEventType): UUID {
        try {
            val userHandlingEvent = UserHandlingEventFactory.getInstance()
                .createUserHandlingEvent(handlingEvent, user)
            userToBackendProducer.produceUserHandlingEvent(userHandlingEvent)
        } catch (uex: Exception) {
            if (uex is UserException) {logger.info(uex)} else throw uex

        }
        return user.userNumber.uuid!!
    }
}