package com.scprojekt.domain.model.user.value

/**
 * Value object for user credentials.
 * This is an immutable object that is identified by its attributes rather than an identity.
 */
data class UserCredentials(val username: String, val password: String) {
    init {
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(username.length >= 3) { "Username must be at least 3 characters" }
        require(password.length >= 8) { "Password must be at least 8 characters" }
        require(password.matches(Regex(".*[A-Z].*"))) { "Password must contain at least one uppercase letter" }
        require(password.matches(Regex(".*[a-z].*"))) { "Password must contain at least one lowercase letter" }
        require(password.matches(Regex(".*[0-9].*"))) { "Password must contain at least one digit" }
    }
}