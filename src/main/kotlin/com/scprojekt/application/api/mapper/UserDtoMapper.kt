package com.scprojekt.application.api.mapper

import com.scprojekt.domain.model.user.User
import com.scprojekt.domain.model.user.UserType
import com.scprojekt.domain.model.user.value.ContactInfo
import com.scprojekt.application.api.dto.ContactInfoDto
import com.scprojekt.application.api.dto.CreateUserDto
import com.scprojekt.application.api.dto.UpdateUserDto
import com.scprojekt.application.api.dto.UserDto
import jakarta.enterprise.context.ApplicationScoped

/**
 * Mapper class to convert between domain User and API DTOs.
 * This class helps decouple the API from the domain model.
 */
@ApplicationScoped
class UserDtoMapper {

    /**
     * Converts a domain User to a UserDto.
     */
    fun toDto(user: User): UserDto {
        return UserDto(
            id = user.id,
            uuid = user.number,
            username = user.name,
            userType = user.type.getRoleType(),
            description = user.description,
            enabled = user.enabled,
            contactInfo = user.contactInfo.map { toDto(it) }
        )
    }

    /**
     * Converts a ContactInfo to a ContactInfoDto.
     */
    fun toDto(contactInfo: ContactInfo): ContactInfoDto {
        return ContactInfoDto(
            email = contactInfo.email,
            phone = contactInfo.phone
        )
    }

    /**
     * Converts a CreateUserDto to a domain User.
     */
    fun toDomain(dto: CreateUserDto): User {
        val userType = UserType.create(
            roleType = dto.userTypeRole,
            description = dto.description
        )
        
        val user = User.create(
            name = dto.username,
            type = userType,
            description = dto.description
        )
        
        dto.contactInfo.forEach { contactInfoDto ->
            user.addContactInfo(toDomain(contactInfoDto))
        }
        
        return user
    }

    /**
     * Converts a ContactInfoDto to a domain ContactInfo.
     */
    fun toDomain(dto: ContactInfoDto): ContactInfo {
        return ContactInfo(
            email = dto.email,
            phone = dto.phone
        )
    }

    /**
     * Updates a domain User with data from an UpdateUserDto.
     */
    fun updateDomain(user: User, dto: UpdateUserDto) {
        dto.username?.let { user.changeName(it) }
        dto.description?.let { user.changeDescription(it) }
        
        if (dto.userTypeRole != null) {
            val userType = UserType.create(
                roleType = dto.userTypeRole,
                description = dto.description ?: user.type.getDescription()
            )
            user.changeUserType(userType)
        }
        
        dto.contactInfo?.let { contactInfoDtos ->
            // Remove existing contact info
            val existingEmails = user.contactInfo.map { it.email }
            existingEmails.forEach { user.removeContactInfo(it) }
            
            // Add new contact info
            contactInfoDtos.forEach { contactInfoDto ->
                user.addContactInfo(toDomain(contactInfoDto))
            }
        }
    }
}