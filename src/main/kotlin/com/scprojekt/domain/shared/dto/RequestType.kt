package com.scprojekt.domain.shared.dto;


enum class RequestType(val value: String) {
    USER("User"),
    CUSTOMER("Customer"),
    ACCOUNT("Account"),
    ASSURANCE("Assurance");

    fun equalsEvent(otherRequest: String): Boolean {
        return value == otherRequest
    }

    override fun toString(): String {
        return value
    }
}

