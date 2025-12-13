package com.scprojekt.domain.model.user

/**
 * Domain class for UserType.
 * This is a rich domain model that encapsulates both data and behavior.
 */
class UserType private constructor(
    val typeId: Long? = null,
    private var roleType: String,
    private var description: String,
    private var enabled: Boolean = true
) {
    companion object {
        fun create(roleType: String, description: String): UserType {
            require(roleType.isNotBlank()) { "Role type cannot be blank" }
            return UserType(
                roleType = roleType,
                description = description
            )
        }
    }
    
    fun changeRoleType(newRoleType: String): UserType {
        require(newRoleType.isNotBlank()) { "Role type cannot be blank" }
        this.roleType = newRoleType
        return this
    }
    
    fun changeDescription(newDescription: String): UserType {
        this.description = newDescription
        return this
    }
    
    fun disable(): UserType {
        this.enabled = false
        return this
    }
    
    fun enable(): UserType {
        this.enabled = true
        return this
    }
    
    // Getters for immutable access
    fun getId(): Long? = typeId
    fun getRoleType(): String = roleType
    fun getDescription(): String = description
    fun isEnabled(): Boolean = enabled
    
    override fun toString(): String {
        return "UserType(roleType='$roleType', description='$description', enabled=$enabled)"
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as UserType
        
        if (roleType != other.roleType) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        return roleType.hashCode()
    }
}