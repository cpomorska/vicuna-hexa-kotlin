package com.scprojekt.infrastructure.mapping

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

class VicunaJacksonMapper : ObjectMapper() {
    companion object {
        fun getInstance(): ObjectMapper {
            val vicunaJacksonMapper = VicunaJacksonMapper()
            vicunaJacksonMapper.init()

            return vicunaJacksonMapper
        }
    }

    private fun init() {
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        this.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
        this.configure(DeserializationFeature.USE_LONG_FOR_INTS, true)
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
    }
}
