package com.scprojekt.infrastructure.messaging

import com.scprojekt.domain.model.user.event.UserHandlingEvent
import com.scprojekt.domain.model.user.service.UserProducer
import com.scprojekt.infrastructure.mapping.VicunaJacksonMapper
import io.smallrye.reactive.messaging.kafka.Record
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import java.util.*

@ApplicationScoped
class UserToBackendProducer : UserProducer {

    @Inject
    @Channel("users-out")
    lateinit var emitter: Emitter<Record<UUID, String>>

    override fun produceUserHandlingEvent(userHandlingEvent: UserHandlingEvent): UUID {
        val serializedUserHandlingEvent = VicunaJacksonMapper.getInstance()?.writeValueAsString(userHandlingEvent)
        emitter.send(Record.of(UUID.randomUUID(), serializedUserHandlingEvent)).whenComplete { success, error ->
            success ?.let {
                storeUserEvent(userHandlingEvent)
            }
            error ?.let {
                storeErrorEvent(userHandlingEvent)
            }
        }

        return userHandlingEvent.eventid!!
    }

    override fun storeUserEvent(userHandlingEvent: UserHandlingEvent): Boolean {
        TODO("Not yet implemented")
    }

    override fun storeErrorEvent(userHandlingEvent: UserHandlingEvent) {
        TODO("Not yet implemented")
    }
}
