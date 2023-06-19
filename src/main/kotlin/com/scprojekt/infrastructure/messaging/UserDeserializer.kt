package com.scprojekt.infrastructure.messaging

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.infrastructure.mapping.VicunaJacksonMapper
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory
import kotlin.text.Charsets.UTF_8


class UserDeserializer : Deserializer<User> {
    private val objectMapper = VicunaJacksonMapper.getInstance()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun deserialize(topic: String?, data: ByteArray?): User? {
        log.info("Deserializing...")
        return objectMapper?.readValue(
            String(
                data ?: throw SerializationException("Error when deserializing byte[] to Product"), UTF_8
            ), User::class.java
        )
    }

    override fun close() {
        // currently used by fuchses
    }
}
