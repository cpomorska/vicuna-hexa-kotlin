package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.model.user.service.UserMessagingService
import com.scprojekt.infrastructure.messaging.UserToBackendProducer
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

    override fun registerNewUser(user: User): UUID {
        return processUser(user, UserEventType.CREATE)
    }

    override fun manageExistingUser(user: User): UUID {
        return processUser(user, UserEventType.MANAGE)
    }

    override fun disableExistingUser(user: User): UUID {
        return processUser(user, UserEventType.DISABLE)
    }

    override fun deleteExistingUser(user: User): UUID {
        return processUser(user, UserEventType.DELETE)
    }
}

