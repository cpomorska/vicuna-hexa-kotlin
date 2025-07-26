package com.scprojekt.domain.model.user.repository

import com.scprojekt.domain.model.user.UserAggregate
import com.scprojekt.domain.model.user.UserType
import java.util.*

/**
 * Repository interface for the User domain model.
 * This interface is defined in the domain layer and implemented in the infrastructure layer.
 */
interface DomainUserRepository {
    /**
     * Finds a user by UUID.
     */
    fun findByUUID(uid: UUID?): UserAggregate?
    
    /**
     * Finds users by UserType.
     */
    fun findByType(userType: UserType): List<UserAggregate>
    
    /**
     * Finds users by name.
     */
    fun findByName(userName: String): List<UserAggregate>
    
    /**
     * Finds users by description.
     */
    fun findByDescription(userDescription: String): List<UserAggregate>
    
    /**
     * Saves a user.
     */
    fun save(userAggregate: UserAggregate): UUID
    
    /**
     * Deletes a user.
     */
    fun delete(userAggregate: UserAggregate): UUID?
}