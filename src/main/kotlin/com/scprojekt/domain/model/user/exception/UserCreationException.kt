package com.scprojekt.domain.model.user.exception

class UserCreationException(e: Throwable, override val userParam: String) : UserException(e, userParam) {
    override val message: String?
        get() = userParam.let { String.format("Error creating user with UserNumber ${it} | ${e.message} ")}
}
