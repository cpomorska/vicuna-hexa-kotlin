package com.scprojekt.domain.model.user.repository

import com.scprojekt.domain.model.user.entity.UserEvent
import com.scprojekt.mimetidae.domain.shared.BaseRepository
import java.util.*

interface UserEventRepository : BaseRepository<UserEvent> {
    fun findByUUID(uuid: UUID?): UserEvent?
    fun findAllToRemove(): MutableList<UserEvent>?
}