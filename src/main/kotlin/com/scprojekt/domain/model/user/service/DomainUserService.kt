package com.scprojekt.domain.model.user.service

import com.scprojekt.domain.model.user.UserAggregate
import com.scprojekt.domain.model.user.UserType
import com.scprojekt.domain.model.user.event.UserHandlingEvent
import com.scprojekt.domain.model.user.value.ContactInfo
import java.util.*

/**
 * Domain service interface for User operations.
 * This service encapsulates complex business logic that doesn't naturally fit in the domain entities.
 */
interface DomainUserService {
    /**
     * Creates a new user.
     */
    fun createUser(name: String, type: UserType, description: String): Pair<UserAggregate, UserHandlingEvent>
    
    /**
     * Updates an existing user.
     */
    fun updateUser(userId: UUID, name: String?, type: UserType?, description: String?): Pair<UserAggregate, UserHandlingEvent>?
    
    /**
     * Disables a user.
     */
    fun disableUser(userId: UUID): Pair<UserAggregate, UserHandlingEvent>?
    
    /**
     * Enables a user.
     */
    fun enableUser(userId: UUID): Pair<UserAggregate, UserHandlingEvent>?
    
    /**
     * Deletes a user.
     */
    fun deleteUser(userId: UUID): UserHandlingEvent?
    
    /**
     * Gets a user by UUID.
     */
    fun getUserByUuid(userId: UUID): UserAggregate?
    
    /**
     * Finds users by type.
     */
    fun findUsersByType(type: UserType): List<UserAggregate>
    
    /**
     * Finds users by name.
     */
    fun findUsersByName(name: String): List<UserAggregate>
    
    /**
     * Finds users by description.
     */
    fun findUsersByDescription(description: String): List<UserAggregate>
    
    /**
     * Adds contact information to a user.
     */
    fun addContactInfo(userId: UUID?, contactInfo: ContactInfo): Pair<UserAggregate, UserHandlingEvent>?
    
    /**
     * Removes contact information from a user.
     */
    fun removeContactInfo(userId: UUID, email: String): Pair<UserAggregate, UserHandlingEvent>?
}