package com.scprojekt.domain.model.user.value

/**
 * Value object for contact information.
 * This is an immutable object that is identified by its attributes rather than an identity.
 */
data class ContactInfo(val email: String, val phone: String?) {
    init {
        require(email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"))) { "Invalid email format" }
        phone?.let {
            require(it.matches(Regex("^\\+?[0-9]{10,15}$"))) { "Invalid phone number format" }
        }
    }
}