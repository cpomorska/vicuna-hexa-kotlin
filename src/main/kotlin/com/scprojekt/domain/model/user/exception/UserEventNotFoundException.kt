package com.scprojekt.mimetidae.domain.model.user.exception

import com.scprojekt.domain.model.user.exception.UserException

class UserEventNotFoundException(e: Throwable, userParam: String) : UserException(e, userParam) {
    override val message: String?
        get() = userParam?.let { String.format("UserEvent ${it} not found | ${e.message} ")}
}
