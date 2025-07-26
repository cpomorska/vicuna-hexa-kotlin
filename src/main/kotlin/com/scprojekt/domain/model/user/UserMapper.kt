package com.scprojekt.domain.model.user.mapper

import com.scprojekt.domain.model.user.User
import com.scprojekt.domain.model.user.UserType
import com.scprojekt.domain.model.user.value.ContactInfo
import com.scprojekt.domain.model.user.value.ContactInfoValueObject
import com.scprojekt.domain.model.user.value.UserTypeValueObject
import com.scprojekt.domain.model.user.value.UserValueObject
import com.scprojekt.infrastructure.persistence.entity.ContactInfoEntity
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserNumberEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity
import jakarta.enterprise.context.ApplicationScoped

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
        
        // Map UserType to UserTypeEntity
        entity.userType = toUserTypeEntity(user.type)
        
        // Map UUID to UserNumberEntity
        entity.userNumber = UserNumberEntity(user.number)
        
        // Map ContactInfo list to ContactInfoEntity list
        entity.contactInfo = user.contactInfo.map { contactInfo ->
            toContactInfoEntity(contactInfo, entity)
        }.toMutableList()
        
        return entity
    }

    /**
     * Converts a persistence UserEntity to a domain User.
     */
    fun toDomain(entity: UserEntity): User {
        val userType = toUserTypeDomain(entity.userType)
        val contactInfoList = entity.contactInfo.map { toContactInfoDomain(it) }
        
        return User(
            id = entity.userId,
            type = userType,
            name = entity.userName,
            number = entity.userNumber.uuid,
            description = entity.userDescription,
            enabled = entity.enabled,
            contactInfo = contactInfoList
        )
    }
    
    /**
     * Converts a domain User to a UserValueObject for API layer.
     */
    fun toValueObject(user: User): UserValueObject {
        return UserValueObject(
            id = user.id,
            type = UserTypeValueObject(
                id = user.type.typeId,
                roleType = user.type.getRoleType(),
                description = user.type.getDescription(),
                enabled = user.type.isEnabled()
            ),
            name = user.name,
            number = user.number,
            description = user.description,
            enabled = user.enabled,
            contactInfo = user.contactInfo.map { 
                ContactInfoValueObject(
                    email = it.email,
                    phone = it.phone
                )
            },
            version = user.version,
            createdAt = user.createdAt,
            modifiedAt = user.modifiedAt
        )
    }
    
    /**
     * Converts a UserValueObject to a domain User.
     */
    fun fromValueObject(valueObject: UserValueObject): User {
        val userType = UserType.create(
            roleType = valueObject.type.roleType,
            description = valueObject.type.description
        )
        
        return User(
            id = valueObject.id,
            type = userType,
            name = valueObject.name,
            number = valueObject.number,
            description = valueObject.description,
            enabled = valueObject.enabled,
            contactInfo = valueObject.contactInfo.map { 
                ContactInfo(
                    email = it.email,
                    phone = it.phone
                )
            },
            version = valueObject.version,
            createdAt = valueObject.createdAt,
            modifiedAt = valueObject.modifiedAt
        )
    }
    
    private fun toUserTypeEntity(userType: UserType): UserTypeEntity {
        val entity = UserTypeEntity()
        entity.userTypeId = userType.typeId
        entity.userRoleType = userType.getRoleType()
        entity.userTypeDescription = userType.getDescription()
        entity.userTypeEnabled = userType.isEnabled()
        return entity
    }
    
    private fun toUserTypeDomain(entity: UserTypeEntity): UserType {
        return UserType.create(
            roleType = entity.userRoleType ?: "",
            description = entity.userTypeDescription ?: ""
        ).let { userType ->
            // If UserType has an id field, set it here
            userType
        }
    }
    
    private fun toContactInfoEntity(contactInfo: ContactInfo, userEntity: UserEntity): ContactInfoEntity {
        val entity = ContactInfoEntity()
        entity.email = contactInfo.email
        entity.phone = contactInfo.phone
        entity.user = userEntity
        return entity
    }
    
    private fun toContactInfoDomain(entity: ContactInfoEntity): ContactInfo {
        return ContactInfo(
            email = entity.email,
            phone = entity.phone
        )
    }
}
