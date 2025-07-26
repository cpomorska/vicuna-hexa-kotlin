package com.scprojekt.domain.model.user.repository

import com.scprojekt.domain.shared.database.BaseRepository
import com.scprojekt.infrastructure.persistence.entity.UserEventEntity
import java.util.*

interface UserEventRepository : BaseRepository<UserEventEntity> {
    fun findByUUID(uuid: UUID?): UserEventEntity?
    fun findAllToRemove(): MutableList<UserEventEntity>?
}