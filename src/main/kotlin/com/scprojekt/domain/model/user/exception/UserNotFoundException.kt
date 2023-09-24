package com.scprojekt.domain.model.user.exception

class UserNotFoundException(e: Throwable, userParam: String) : UserException(e, userParam) {
    override val message: String?
        get() = userParam?.let { String.format("User with parameter ${it} not found | ${e.message} ")}
}
