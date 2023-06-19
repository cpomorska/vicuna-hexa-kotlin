package com.scprojekt.domain.model.user.exception;

import com.scprojekt.domain.model.user.entity.UserNumber


class UserDeletionException(override val e: Throwable, override val userNumber: UserNumber) : UserException(e, userNumber) {
	override val message: String?
		get() = userNumber.let { String.format("Error deleting user with UserNumber ${it}?:'null' | ${e.message}", it) }
}
