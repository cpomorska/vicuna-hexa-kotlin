package com.scprojekt.domain.model.user.service

import com.scprojekt.domain.shared.service.BaseService
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import java.util.*

interface UserMessagingService: BaseService<UserEntity> {
    fun registerNewUser(userEntity: UserEntity): UUID
    fun manageExistingUser(userEntity: UserEntity): UUID
    fun disableExistingUser(userEntity: UserEntity): UUID
    fun deleteExistingUser(userEntity: UserEntity): UUID
}