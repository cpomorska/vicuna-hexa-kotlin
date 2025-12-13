package com.scprojekt.domain.model.user.service

import com.scprojekt.domain.shared.service.BaseService
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity

interface UserReadOnlyService: BaseService<UserEntity> {
    fun getUserByUuid(userUuid: String): UserEntity?
    fun findAllUsersByType(userTypeEntity: UserTypeEntity): List<UserEntity>
    fun findAllUserByName(userName: String): List<UserEntity>
    fun findAllUserByDescription(userDescription: String): List<UserEntity>
}
