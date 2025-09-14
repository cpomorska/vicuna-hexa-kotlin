package com.scprojekt.infrastructure.messaging

import com.scprojekt.infrastructure.mapping.VicunaObjectMapper
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory
import kotlin.text.Charsets.UTF_8


class UserDeserializer : Deserializer<UserEntity> {
    private val objectMapper = VicunaObjectMapper.getInstance()
    private val log = LoggerFactory.getLogger(javaClass)

    override fun deserialize(topic: String?, data: ByteArray?): UserEntity? {
        log.info("Deserializing...")
        return objectMapper.readValue(
            String(
                data ?: throw SerializationException("Error when deserializing to User"), UTF_8
            ), UserEntity::class.java
        )
    }

    override fun close() {
        // currently used by fuchses
    }
}
