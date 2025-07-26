package com.scprojekt.infrastructure.api.dto

/**
 * DTO for user creation.
 */
data class CreateUserDto(
    val username: String,
    val userTypeId: Long?,
    val userTypeRole: String,
    val description: String,
    val contactInfo: List<ContactInfoDto> = emptyList()
)