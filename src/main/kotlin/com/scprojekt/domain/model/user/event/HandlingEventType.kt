package com.scprojekt.domain.model.user.event

enum class HandlingEventType(val eventType: String) {
    CREATEUSER("Create User"),
    UPDATEUSER("Update User"),
    DELETEUSER("Delete User"),
    MANAGEUSER("Manage User"),
    DISABLEUSER("Disable User")
}
