package com.scprojekt.domain.model.user.exception;

import com.scprojekt.domain.model.user.entity.UserNumber

class UserCreationException(e: Throwable, userNumber: UserNumber) : UserException(e, userNumber) {
    override val message: String?
        get() = userNumber?.let { String.format("Error creating user with UserNumber ${it.uuid} | ${e.message} ")}
}
