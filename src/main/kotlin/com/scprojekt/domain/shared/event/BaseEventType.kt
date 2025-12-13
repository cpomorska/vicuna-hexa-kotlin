package com.scprojekt.domain.shared.event

enum class BaseEventType(val eventType: String) {
    CREATE("Create"),
    UPDATE("Update"),
    DELETE("Delete"),
    MANAGE("Manage"),
    DISABLE("Disable"),
    READ("Read"),
    NONE("None")
}
