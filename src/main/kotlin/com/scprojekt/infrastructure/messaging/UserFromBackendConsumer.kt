package com.scprojekt.infrastructure.messaging

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.infrastructure.mapping.VicunaJacksonMapper
import io.smallrye.reactive.messaging.kafka.Record
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Acknowledgment
import org.eclipse.microprofile.reactive.messaging.Incoming
import java.util.*

@ApplicationScoped
class UserFromBackendConsumer {
    @Incoming("users-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    fun receive(record: Record<UUID?, String?>): User? {
        val user: User? = VicunaJacksonMapper.getInstance().readValue(record.value().toString(), User::class.java)
        return user
    }
}
