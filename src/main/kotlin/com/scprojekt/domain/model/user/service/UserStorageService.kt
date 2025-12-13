package com.scprojekt.domain.model.user.service

import com.scprojekt.domain.model.user.dto.response.UuidResponse
import com.scprojekt.domain.shared.service.BaseService
import com.scprojekt.infrastructure.persistence.entity.UserEntity

interface UserStorageService: BaseService<UserEntity> {
    fun createUser(userEntity: UserEntity): UuidResponse
    fun updateUser(userEntity: UserEntity): UuidResponse
    fun removeUser(userEntity: UserEntity): UuidResponse
}
