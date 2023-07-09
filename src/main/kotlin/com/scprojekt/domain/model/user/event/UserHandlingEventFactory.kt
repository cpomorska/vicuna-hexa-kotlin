package com.scprojekt.domain.model.user.event

import com.scprojekt.domain.model.user.entity.User
import java.util.*

class UserHandlingEventFactory {
    companion object {
        @JvmStatic
        fun getInstance(): UserHandlingEventFactory {
            val handlingEventFactory = UserHandlingEventFactory()
            return handlingEventFactory
        }
    }

    fun createUserHandlingEvent(handlingEventType: HandlingEventType, user: User): UserHandlingEvent {
        val userCreationEvent = UserHandlingEvent()
        val userHandlingEventType = UserHandlingEventType()

        userHandlingEventType.eventType = handlingEventType
        userHandlingEventType.eventDescription = handlingEventType.eventType

        userCreationEvent.eventid = UUID.randomUUID()
        userCreationEvent.userHandlingEventType = userHandlingEventType
        userCreationEvent.user = user
        userCreationEvent.eventTriggeredTimestamp = getTimestampFromDateTime()

        return userCreationEvent
    }

    private fun getTimestampFromDateTime(): Long {
        val currentDateTime = Date()
        return currentDateTime.time
    }
}