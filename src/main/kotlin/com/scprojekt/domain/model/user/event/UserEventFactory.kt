package com.scprojekt.domain.model.user.event

import com.scprojekt.domain.model.user.entity.User
import java.util.*

/**
 * Factory for UserEvents
 */
class UserEventFactory {
    companion object {
        @JvmStatic
        fun getInstance(): UserEventFactory {
            val eventFactory = UserEventFactory()
            return eventFactory
        }
    }

    fun createUserEvent(userEventType: UserEventType, user: User): UserHandlingEvent {
        val userHandlingEvent = UserHandlingEvent()
        val userHandlingEventType = UserHandlingEventType()

        userHandlingEventType.userEventType = userEventType
        userHandlingEventType.eventDescription = userEventType.eventType

        userHandlingEvent.eventid = UUID.randomUUID()
        userHandlingEvent.userHandlingEventType = userHandlingEventType
        userHandlingEvent.user = user
        userHandlingEvent.eventTriggeredTimestamp = getTimestampFromDateTime()

        return userHandlingEvent
    }

    private fun getTimestampFromDateTime(): Long {
        val currentDateTime = Date()
        return currentDateTime.time
    }
}