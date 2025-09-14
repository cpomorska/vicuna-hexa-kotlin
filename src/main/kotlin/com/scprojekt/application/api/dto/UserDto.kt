package com.scprojekt.application.api.dto

import java.util.*

/**
 * DTO for User response.
 * This class is used to transfer user data to the API clients without exposing the domain model.
 */
data class UserDto(
    val id: Long?,
    val uuid: UUID?,
    val username: String,
    val userType: String,
    val description: String,
    val enabled: Boolean,
    val contactInfo: List<ContactInfoDto> = emptyList()
)

