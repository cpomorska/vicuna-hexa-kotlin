package com.scprojekt.domain.shared.event

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Consumer


class BaseEventTypeTest {

    @Test
    fun testToString() {
        val enumList = EnumSet.allOf(BaseEventType::class.java)
        val eventlist: List<String> = enumList.stream().map<String> { e: BaseEventType -> e.name }
            .toList()
        enumList.forEach(Consumer { e: BaseEventType ->
            assertThat(
                eventlist.contains(e.toString())
            )
        })
    }

    @Test
    fun values() {
        val enumList = EnumSet.allOf(BaseEventType::class.java)
        assertThat(BaseEventType.values()).hasSize(enumList.size)
    }

    @Test
    fun valueOf() {
        val enumMap: MutableMap<String, BaseEventType> = HashMap()
        val enumList = EnumSet.allOf(BaseEventType::class.java)

        enumList.forEach(Consumer { e: BaseEventType ->
            enumMap[e.name] = e
        })

        val eventlist: List<String> = enumList.stream().map<String> { e: BaseEventType -> e.name }.toList()

        eventlist.forEach(Consumer<String> { e: String ->
            assertThat(BaseEventType.valueOf(e.uppercase(Locale.getDefault())))
                .isIn(enumList)
        })
    }
}