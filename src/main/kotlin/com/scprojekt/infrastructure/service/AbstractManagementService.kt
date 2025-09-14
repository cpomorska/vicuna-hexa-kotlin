package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.event.UserEventFactory
import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.model.user.exception.UserException
import com.scprojekt.domain.shared.service.BaseService
import com.scprojekt.infrastructure.messaging.UserToBackendProducer
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import io.vertx.core.impl.logging.Logger
import io.vertx.core.impl.logging.LoggerFactory
import lombok.extern.slf4j.Slf4j
import java.util.*

@Slf4j
abstract class AbstractManagementService(private val userToBackendProducer: UserToBackendProducer): BaseService<UserEntity>{
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    protected fun processUser(userEntity: UserEntity, userEvent: UserEventType): UUID {
        try {
            val userHandlingEvent = UserEventFactory.getInstance()
                .createUserEvent(userEvent, userEntity)
            userToBackendProducer.produceUserHandlingEvent(userHandlingEvent)
        } catch (uex: Exception) {
            if (uex is UserException) {logger.info(uex)} else throw uex

        }
        return userEntity.userNumber.uuid!!
    }
}