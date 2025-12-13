package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.model.user.service.UserMessagingService
import com.scprojekt.infrastructure.messaging.UserToBackendProducer
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import io.vertx.core.impl.logging.Logger
import io.vertx.core.impl.logging.LoggerFactory
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.*

@ApplicationScoped
class UserManagementService(
    @Inject
    var userToBackendProducer: UserToBackendProducer
) : AbstractManagementService(userToBackendProducer), UserMessagingService {

    private val logger: Logger = LoggerFactory.getLogger(UserMessagingService::class.java)

    override fun registerNewUser(userEntity: UserEntity): UUID {
        return processUser(userEntity, UserEventType.CREATE)
    }

    override fun manageExistingUser(userEntity: UserEntity): UUID {
        return processUser(userEntity, UserEventType.MANAGE)
    }

    override fun disableExistingUser(userEntity: UserEntity): UUID {
        return processUser(userEntity, UserEventType.DISABLE)
    }

    override fun deleteExistingUser(userEntity: UserEntity): UUID {
        return processUser(userEntity, UserEventType.DELETE)
    }
}

