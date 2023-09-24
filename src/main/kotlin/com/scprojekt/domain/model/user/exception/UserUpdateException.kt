package com.scprojekt.domain.model.user.exception

class UserUpdateException(e: Throwable, override val userParam: String?) : UserException(e, userParam) {
    override val message: String?
        get() = userParam?.let { String.format("Error updating user with UserNumber ${it} | ${e.message} ")}
}
