package com.scprojekt.infrastructure.messaging

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.infrastructure.mapping.VicunaObjectMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer


class UserDeserializer : Deserializer<User> {
    private val objectMapper = VicunaObjectMapper.getInstance()

    override fun deserialize(topic: String?, data: ByteArray?): User? {
        val bytes = data ?: throw SerializationException("Error when deserializing to User")
        return objectMapper.readValue(bytes, User::class.java)
    }

    override fun close() {
        // currently used by fuchses
    }
}
