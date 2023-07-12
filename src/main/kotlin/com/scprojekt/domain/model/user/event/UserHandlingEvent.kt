package com.scprojekt.domain.model.user.event

import com.scprojekt.domain.model.user.entity.User
import java.util.*


class UserHandlingEvent {
    var eventid: UUID? = null
    var user: User? = null
    var userHandlingEventType: UserHandlingEventType? = null
    var eventTriggeredTimestamp: Long? = null
}
