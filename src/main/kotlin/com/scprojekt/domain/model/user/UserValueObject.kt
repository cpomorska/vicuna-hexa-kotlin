package com.scprojekt.domain.model.user.value

import java.time.Instant
import java.util.*

/**
 * Value object representing user data for API layer.
 * This is immutable and contains only the essential user information.
 */
data class UserValueObject(
    val id: Long?,
    val type: UserTypeValueObject,
    val name: String,
    val number: UUID?,
    val description: String,
    val enabled: Boolean,
    val contactInfo: List<ContactInfoValueObject>,
    val version: Long?,
    val createdAt: Instant?,
    val modifiedAt: Instant?
)

data class UserTypeValueObject(
    val id: Long?,
    val roleType: String,
    val description: String,
    val enabled: Boolean
)

data class ContactInfoValueObject(
    val email: String,
    val phone: String?
)
