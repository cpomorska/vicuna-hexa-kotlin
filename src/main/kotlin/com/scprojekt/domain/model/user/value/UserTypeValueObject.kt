package com.scprojekt.domain.model.user.value

data class UserTypeValueObject(
    val id: Long?,
    val roleType: String,
    val description: String,
    val enabled: Boolean
)