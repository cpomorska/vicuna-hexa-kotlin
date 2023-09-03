package com.scprojekt.domain.model.user.repository

import com.scprojekt.domain.model.user.entity.UserEventStore
import java.util.*

interface UserStoreRepository : BaseRepository<UserEventStore>{
    fun findByUUID(uuid: UUID?): UserEventStore?
}