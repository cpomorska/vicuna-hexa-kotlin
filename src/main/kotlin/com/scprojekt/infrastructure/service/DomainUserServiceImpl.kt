package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.UserAggregate
import com.scprojekt.domain.model.user.UserType
import com.scprojekt.domain.model.user.event.UserEventFactory
import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.model.user.event.UserHandlingEvent
import com.scprojekt.domain.model.user.repository.DomainUserRepository
import com.scprojekt.domain.model.user.service.DomainUserService
import com.scprojekt.domain.model.user.value.ContactInfo
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserNumberEntity
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import java.util.*

/**
 * Implementation of the DomainUserService interface.
 * This service encapsulates complex business logic that doesn't naturally fit in the domain entities.
 */
@ApplicationScoped
@Transactional
class DomainUserServiceImpl @Inject constructor(
    private val userRepository: DomainUserRepository
) : DomainUserService {

    override fun createUser(name: String, type: UserType, description: String): Pair<UserAggregate, UserHandlingEvent> {
        val (userAggregate, event) = UserAggregate.create(name, type, description)
        userRepository.save(userAggregate)
        return Pair(userAggregate, event)
    }

    override fun updateUser(
        userId: UUID,
        name: String?,
        type: UserType?,
        description: String?
    ): Pair<UserAggregate, UserHandlingEvent>? {
        val userAggregate = userRepository.findByUUID(userId) ?: return null
        
        var event: UserHandlingEvent? = null
        
        if (name != null) {
            event = userAggregate.changeName(name)
        }
        
        if (type != null) {
            event = userAggregate.changeUserType(type)
        }
        
        if (description != null) {
            event = userAggregate.changeDescription(description)
        }
        
        if (event != null) {
            userRepository.save(userAggregate)
            return Pair(userAggregate, event)
        }
        
        return null
    }

    override fun disableUser(userId: UUID): Pair<UserAggregate, UserHandlingEvent>? {
        val userAggregate = userRepository.findByUUID(userId) ?: return null
        val event = userAggregate.disable()
        userRepository.save(userAggregate)
        return Pair(userAggregate, event)
    }

    override fun enableUser(userId: UUID): Pair<UserAggregate, UserHandlingEvent>? {
        val userAggregate = userRepository.findByUUID(userId) ?: return null
        val event = userAggregate.enable()
        userRepository.save(userAggregate)
        return Pair(userAggregate, event)
    }

    override fun deleteUser(userId: UUID): UserHandlingEvent? {
        val userAggregate = userRepository.findByUUID(userId) ?: return null
        val user = userAggregate.getUser()
        userRepository.delete(userAggregate)
        
        // Create a delete event
        return UserEventFactory.getInstance().createUserEvent(
            UserEventType.DELETE,
            UserEntity().apply {
                this.userNumber = UserNumberEntity(userId)
            }
        )
    }

    override fun getUserByUuid(userId: UUID): UserAggregate? {
        return userRepository.findByUUID(userId)
    }

    override fun findUsersByType(type: UserType): List<UserAggregate> {
        return userRepository.findByType(type)
    }

    override fun findUsersByName(name: String): List<UserAggregate> {
        return userRepository.findByName(name)
    }

    override fun findUsersByDescription(description: String): List<UserAggregate> {
        return userRepository.findByDescription(description)
    }

    override fun addContactInfo(userId: UUID?, contactInfo: ContactInfo): Pair<UserAggregate, UserHandlingEvent>? {
        val userAggregate = userRepository.findByUUID(userId) ?: return null
        val event = userAggregate.addContactInfo(contactInfo)
        userRepository.save(userAggregate)
        return Pair(userAggregate, event)
    }

    override fun removeContactInfo(userId: UUID, email: String): Pair<UserAggregate, UserHandlingEvent>? {
        val userAggregate = userRepository.findByUUID(userId) ?: return null
        val event = userAggregate.removeContactInfo(email)
        userRepository.save(userAggregate)
        return Pair(userAggregate, event)
    }
}