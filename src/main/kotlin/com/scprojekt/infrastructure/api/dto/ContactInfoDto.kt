package com.scprojekt.infrastructure.api.dto

/**
 * DTO for ContactInfo response.
 */
data class ContactInfoDto(
    val email: String,
    val phone: String?
)