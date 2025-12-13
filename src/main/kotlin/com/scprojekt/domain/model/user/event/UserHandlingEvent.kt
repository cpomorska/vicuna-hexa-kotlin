package com.scprojekt.domain.model.user.event

import com.scprojekt.infrastructure.persistence.entity.UserEntity
import java.util.*


class UserHandlingEvent {
    lateinit var eventid: UUID
    lateinit var userEntity: UserEntity
    lateinit var userHandlingEventType: UserHandlingEventType
    var eventTriggeredTimestamp: Long? = 1L
}
