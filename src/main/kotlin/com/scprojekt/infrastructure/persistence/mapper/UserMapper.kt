package com.scprojekt.infrastructure.persistence.mapper

import com.scprojekt.domain.model.user.User
import com.scprojekt.domain.model.user.UserType
import com.scprojekt.domain.model.user.value.ContactInfo
import com.scprojekt.infrastructure.persistence.entity.ContactInfoEntity
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserNumberEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity
import jakarta.enterprise.context.ApplicationScoped
import java.util.*

/**
 * Mapper class to convert between domain User and persistence UserEntity.
 * This class helps achieve persistence ignorance by separating the domain model from the persistence concerns.
 */
@ApplicationScoped
class UserMapper {

    /**
     * Converts a domain User to a persistence UserEntity.
     */
    fun toEntity(user: User): UserEntity {
        val entity = UserEntity()
        entity.userId = user.id
        entity.userName = user.name
        entity.userDescription = user.description
        entity.version = user.version as Int? ?: 0
        entity.enabled = user.enabled
        
        // Map UserType to UserTypeEntity
        val userTypeEntity = UserTypeEntity()
        userTypeEntity.userTypeId = user.type.getId()
        userTypeEntity.userRoleType = user.type.getRoleType()
        userTypeEntity.userTypeDescription = user.type.getDescription()
        userTypeEntity.userTypeEnabled = user.type.isEnabled()
        entity.userType = userTypeEntity
        
        // Map UUID to UserNumberEntity
        val userNumberEntity = UserNumberEntity(user.number ?: UUID.randomUUID())
        entity.userNumber = userNumberEntity
        
        // Map ContactInfo list to ContactInfoEntity list
        entity.contactInfo = user.contactInfo.map { contactInfo ->
            val contactInfoEntity = ContactInfoEntity()
            contactInfoEntity.email = contactInfo.email
            contactInfoEntity.phone = contactInfo.phone
            contactInfoEntity.user = entity
            contactInfoEntity
        }.toMutableList()
        
        return entity
    }

    /**
     * Converts a persistence UserEntity to a domain User.
     */
    fun toDomain(entity: UserEntity): User {
        // Create UserType domain object
        val userType = UserType.create(
            roleType = entity.userType.userRoleType ?: "",
            description = entity.userType.userTypeDescription ?: ""
        )
        
        // Create User domain object
        val user = User.create(
            name = entity.userName,
            type = userType,
            description = entity.userDescription
        )
        
        // Set additional properties using reflection (since they're private)
        val userClass = User::class.java
        
        // Set ID
        val idField = userClass.getDeclaredField("id")
        idField.isAccessible = true
        idField.set(user, entity.userId)
        
        // Set UUID
        val numberField = userClass.getDeclaredField("number")
        numberField.isAccessible = true
        numberField.set(user, entity.userNumber.uuid)
        
        // Set version
        val versionField = userClass.getDeclaredField("version")
        versionField.isAccessible = true
        versionField.set(user, entity.version)
        
        // Set enabled
        val enabledField = userClass.getDeclaredField("enabled")
        enabledField.isAccessible = true
        enabledField.set(user, entity.enabled)
        
        // Set createdAt
        val createdAtField = userClass.getDeclaredField("createdAt")
        createdAtField.isAccessible = true
        createdAtField.set(user, entity.createdAt)
        
        // Set modifiedAt
        val modifiedAtField = userClass.getDeclaredField("modifiedAt")
        modifiedAtField.isAccessible = true
        modifiedAtField.set(user, entity.modifiedAt)
        
        // Add ContactInfo objects
        entity.contactInfo.forEach { contactInfoEntity ->
            user.addContactInfo(ContactInfo(
                email = contactInfoEntity.email,
                phone = contactInfoEntity.phone
            ))
        }
        
        return user
    }
}