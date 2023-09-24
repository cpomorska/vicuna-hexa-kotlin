package com.scprojekt.domain.model.user.event

import com.scprojekt.domain.model.user.entity.User
import java.util.*


class UserHandlingEvent {
    lateinit var eventid: UUID
    lateinit var user: User
    lateinit var userHandlingEventType: UserHandlingEventType
    var eventTriggeredTimestamp: Long? = 1L
}
