package com.scprojekt.domain.model.user.repository

import com.scprojekt.domain.shared.database.BaseRepository
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity

interface UserRepository : BaseRepository<UserEntity> {
    fun findByUUID(uid: String): UserEntity?
    fun findByType(userTypeEntity: UserTypeEntity): List<UserEntity>
    fun findByName(userName: String): List<UserEntity>
    fun findByDescription(userDescription: String): List<UserEntity>
}
