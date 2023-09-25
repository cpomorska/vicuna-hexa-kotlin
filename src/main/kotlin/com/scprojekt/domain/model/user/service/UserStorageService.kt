package com.scprojekt.domain.model.user.service

import com.scprojekt.domain.model.user.dto.UuidResponse
import com.scprojekt.domain.model.user.entity.User

interface UserStorageService {
    fun createUser(user: User): UuidResponse
    fun updateUser(user: User): UuidResponse
    fun removeUser(user: User): UuidResponse
}
