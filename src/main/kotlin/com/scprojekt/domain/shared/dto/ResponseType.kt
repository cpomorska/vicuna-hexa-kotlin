package com.scprojekt.domain.shared.dto;


enum class ResponseType(val value: String) {
    USER("User"),
    CUSTOMER("Customer"),
    ACCOUNT("Account"),
    ASSURANCE("Assurance");

    fun equalsEvent(otherResponse: String): Boolean {
        return value == otherResponse
    }

    override fun toString(): String {
        return value
    }
}

