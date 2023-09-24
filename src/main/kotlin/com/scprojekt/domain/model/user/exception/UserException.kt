package com.scprojekt.domain.model.user.exception

import lombok.NoArgsConstructor

@NoArgsConstructor
open class UserException(open val e: Throwable, open val userParam: String?) : RuntimeException(e) {
    override val message: String?
        get() = userParam?.let { String.format("Userexception occured with parameter ${it}?:'null' | ${e.message}", it) }
}
