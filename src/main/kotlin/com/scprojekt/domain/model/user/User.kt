package com.scprojekt.domain.model.user


import com.scprojekt.domain.model.user.value.ContactInfo
import com.scprojekt.domain.model.user.value.UserCredentials
import java.time.Instant
import java.util.*

/**
 * User domain entity that represents a user in the system.
 * This is a rich domain model that encapsulates both data and behavior.
 */
data class User(
    val id: Long? = null,
    val type: UserType,
    val name: String,
    val number: UUID?,
    val description: String,
    val enabled: Boolean = true,
    val contactInfo: List<ContactInfo> = emptyList(),
    val version: Long? = null,
    val createdAt: Instant? = null,
    val modifiedAt: Instant? = null
) {
    companion object {
        fun create(
            name: String,
            type: UserType,
            description: String
        ): User {
            require(name.isNotBlank()) { "Username cannot be blank" }
            require(name.length >= 3) { "Username must be at least 3 characters" }
            
            return User(
                type = type,
                name = name,
                number = UUID.randomUUID(),
                description = description
            )
        }
        
        fun createWithCredentials(
            credentials: UserCredentials,
            type: UserType,
            description: String
        ): User {
            return User(
                type = type,
                name = credentials.username,
                number = UUID.randomUUID(),
                description = description
            )
        }
    }
    
    init {
        require(name.isNotBlank()) { "Username cannot be blank" }
        require(name.length >= 3) { "Username must be at least 3 characters" }
    }
    
    fun changeUserType(newType: UserType): User {
        return this.copy(type = newType)
    }
    
    fun changeName(newName: String): User {
        require(newName.isNotBlank()) { "Username cannot be blank" }
        require(validateName(newName)) { "Invalid username format" }
        
        return this.copy(name = newName)
    }
    
    fun changeDescription(newDescription: String): User {
        return this.copy(description = newDescription)
    }
    
    fun disable(): User {
        return this.copy(enabled = false)
    }
    
    fun enable(): User {
        return this.copy(enabled = true)
    }
    
    fun addContactInfo(info: ContactInfo): User {
        return this.copy(contactInfo = this.contactInfo + info)
    }
    
    fun removeContactInfo(email: String): User {
        return this.copy(contactInfo = this.contactInfo.filterNot { it.email == email })
    }
    
    private fun validateName(name: String): Boolean {
        return name.length >= 3
    }
    
    fun withPersistenceData(
        id: Long,
        version: Long,
        createdAt: Instant,
        modifiedAt: Instant
    ): User {
        return this.copy(
            id = id,
            version = version,
            createdAt = createdAt,
            modifiedAt = modifiedAt
        )
    }
}
