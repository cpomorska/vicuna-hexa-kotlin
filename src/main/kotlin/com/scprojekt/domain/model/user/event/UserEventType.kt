package com.scprojekt.domain.model.user.event

enum class UserEventType(val eventType: String) {
    CREATE("Create"),
    UPDATE("Update"),
    DELETE("Delete"),
    MANAGE("Manage"),
    DISABLE("Disable"),
    READ("Read"),
    NONE("None")
}
