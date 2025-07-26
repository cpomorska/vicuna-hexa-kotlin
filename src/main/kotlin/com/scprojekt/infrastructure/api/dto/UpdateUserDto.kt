package com.scprojekt.infrastructure.api.dto

import java.util.UUID

/**
 * DTO for user update.
 */
data class UpdateUserDto(
    val id: Long?,
    val uuid: UUID?,
    val username: String?,
    val userTypeId: Long?,
    val userTypeRole: String?,
    val description: String?,
    val contactInfo: List<ContactInfoDto>? = null
)