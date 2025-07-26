package com.scprojekt.domain.model.user.event

import com.scprojekt.infrastructure.persistence.entity.UserEntity
import java.util.*

/**
 * Factory for UserEvents
 */
class UserEventFactory {
    companion object {
        @JvmStatic
        fun getInstance(): UserEventFactory {
            return  UserEventFactory()
        }
    }

    fun createUserEvent(userEventType: UserEventType, userEntity: UserEntity): UserHandlingEvent {
        val userHandlingEvent = UserHandlingEvent()
        val userHandlingEventType = UserHandlingEventType()

        userHandlingEventType.userEventType = userEventType
        userHandlingEventType.eventDescription = userEventType.eventType

        userHandlingEvent.eventid = UUID.randomUUID()
        userHandlingEvent.userHandlingEventType = userHandlingEventType
        userHandlingEvent.userEntity = userEntity
        userHandlingEvent.eventTriggeredTimestamp = getTimestampFromDateTime()

        return userHandlingEvent
    }

    private fun getTimestampFromDateTime(): Long {
        val currentDateTime = Date()
        return currentDateTime.time
    }
}