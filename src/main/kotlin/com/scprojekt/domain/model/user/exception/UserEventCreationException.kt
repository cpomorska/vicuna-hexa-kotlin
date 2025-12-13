package com.scprojekt.mimetidae.domain.model.user.exception

import com.scprojekt.domain.model.user.exception.UserException

class UserEventCreationException(e: Throwable, override val userParam: String) : UserException(e, userParam) {
    override val message: String
        get() = userParam.let { String.format("Error creating user event ${it} | ${e.message} ")}
}
