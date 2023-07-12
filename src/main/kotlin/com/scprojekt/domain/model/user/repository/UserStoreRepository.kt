package com.scprojekt.domain.model.user.repository

import com.scprojekt.domain.model.user.entity.UserEventStore

interface UserStoreRepository : BaseRepository<UserEventStore>{
    fun findByUUID(uuid: String): UserEventStore?
}