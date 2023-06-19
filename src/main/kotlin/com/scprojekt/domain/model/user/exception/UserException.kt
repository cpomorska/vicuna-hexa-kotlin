package com.scprojekt.domain.model.user.exception;

import com.scprojekt.domain.model.user.entity.UserNumber
import lombok.NoArgsConstructor

@NoArgsConstructor
open class UserException(open val e: Throwable, open val userNumber: UserNumber?) : Exception(e) {
    override val message: String?
        get() = userNumber?.let { String.format("Userexception occured with UserNumber ${it}?:'null' | ${e.message}", it) }
}
