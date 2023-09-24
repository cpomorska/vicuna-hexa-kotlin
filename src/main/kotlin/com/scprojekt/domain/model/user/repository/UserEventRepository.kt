package com.scprojekt.domain.model.user.repository

import com.scprojekt.domain.model.user.entity.UserEventStore
import com.scprojekt.mimetidae.domain.shared.BaseRepository
import java.util.*

interface UserEventRepository : BaseRepository<UserEventStore> {
    fun findByUUID(uuid: UUID?): UserEventStore?
    fun findAllToRemove(): MutableList<UserEventStore>?
}