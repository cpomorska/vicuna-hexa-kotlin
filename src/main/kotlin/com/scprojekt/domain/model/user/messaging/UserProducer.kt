package com.scprojekt.domain.model.user.messaging

import com.scprojekt.domain.model.user.event.UserHandlingEvent
import java.util.*

interface UserProducer {
   fun produceUserHandlingEvent(userHandlingEvent: UserHandlingEvent): UUID
   fun storeUserEvent(userHandlingEvent: UserHandlingEvent): Boolean
   fun storeErrorEvent(userHandlingEvent: UserHandlingEvent)
}