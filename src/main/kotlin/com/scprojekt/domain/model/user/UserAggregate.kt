package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.event.UserEventFactory
import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.model.user.event.UserHandlingEvent
import com.scprojekt.domain.model.user.value.ContactInfo
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserNumberEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity
import java.util.*

/**
 * UserAggregate is the aggregate root for the User aggregate.
 * It enforces the aggregate boundaries and ensures that all changes to entities within the aggregate
 * go through the aggregate root.
 */
class UserAggregate private constructor(
    private val user: User
) {
    companion object {
        fun create(
            name: String,
            type: UserType,
            description: String
        ): Pair<UserAggregate, UserHandlingEvent> {
            val user = User.create(
                name = name,
                type = type,
                description = description
            )
            
            val event = UserEventFactory.getInstance().createUserEvent(UserEventType.CREATE, toEntity(user))
            return Pair(UserAggregate(user), event)
        }
        
        fun fromUser(user: User): UserAggregate {
            return UserAggregate(user)
        }
        
        // Helper method to convert domain User to entity User for event creation
        private fun toEntity(user: User): UserEntity {
            val entity = UserEntity()
            entity.userId = user.id
            entity.userName = user.name
            entity.userDescription = user.description
            
            // Create UserType entity
            val userTypeEntity = UserTypeEntity()
            userTypeEntity.userTypeId = user.type.typeId
            userTypeEntity.userRoleType = user.type.getRoleType()
            userTypeEntity.userTypeDescription = user.type.getDescription()
            entity.userType = userTypeEntity
            
            // Create UserNumber entity
            val userNumberEntity = UserNumberEntity()
            userNumberEntity.uuid = user.number
            entity.userNumber = userNumberEntity
            
            return entity
        }
    }
    
    fun getUser(): User = user
    
    fun changeUserType(newType: UserType): UserHandlingEvent {
        user.changeUserType(newType)
        return UserEventFactory.getInstance().createUserEvent(UserEventType.UPDATE, toEntity(user))
    }
    
    fun changeName(newName: String): UserHandlingEvent {
        user.changeName(newName)
        return UserEventFactory.getInstance().createUserEvent(UserEventType.UPDATE, toEntity(user))
    }
    
    fun changeDescription(newDescription: String): UserHandlingEvent {
        user.changeDescription(newDescription)
        return UserEventFactory.getInstance().createUserEvent(UserEventType.UPDATE, toEntity(user))
    }
    
    fun disable(): UserHandlingEvent {
        user.disable()
        return UserEventFactory.getInstance().createUserEvent(UserEventType.DISABLE, toEntity(user))
    }
    
    fun enable(): UserHandlingEvent {
        user.enable()
        return UserEventFactory.getInstance().createUserEvent(UserEventType.ENABLE, toEntity(user))
    }
    
    fun addContactInfo(info: ContactInfo): UserHandlingEvent {
        user.addContactInfo(info)
        return UserEventFactory.getInstance().createUserEvent(UserEventType.UPDATE, toEntity(user))
    }
    
    fun removeContactInfo(email: String): UserHandlingEvent {
        user.removeContactInfo(email)
        return UserEventFactory.getInstance().createUserEvent(UserEventType.UPDATE, toEntity(user))
    }
    
    // Delegate methods to the User entity for immutable access
    fun getId(): Long? = user.id
    fun getType(): UserType = user.type
    fun getName(): String = user.name
    fun getNumber(): UUID? = user.number
    fun getDescription(): String = user.description
    fun isEnabled(): Boolean = user.enabled
    fun getContactInfo(): List<ContactInfo> = user.contactInfo
}