package com.scprojekt.domain.model.user.service

import com.scprojekt.domain.model.user.dto.UuidResponse
import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.shared.service.BaseService

interface UserStorageService: BaseService<User> {
    fun createUser(user: User): UuidResponse
    fun updateUser(user: User): UuidResponse
    fun removeUser(user: User): UuidResponse
}
