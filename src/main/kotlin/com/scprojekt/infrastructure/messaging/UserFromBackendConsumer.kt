package com.scprojekt.infrastructure.messaging

import com.scprojekt.infrastructure.mapping.VicunaObjectMapper
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import io.smallrye.reactive.messaging.kafka.Record
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Acknowledgment
import org.eclipse.microprofile.reactive.messaging.Incoming
import java.util.*

@ApplicationScoped
class UserFromBackendConsumer {
    @Incoming("users-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    fun receive(record: Record<UUID?, String?>): UserEntity? {
        val userEntity: UserEntity? = VicunaObjectMapper.getInstance().readValue(record.value().toString(), UserEntity::class.java)
        return userEntity
    }
}
