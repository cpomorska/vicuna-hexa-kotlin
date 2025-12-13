package com.scprojekt.domain.model.user.exception


class UserDeletionException(override val e: Throwable, override val userParam: String) : UserException(e, userParam) {
	override val message: String
		get() = userParam.let { String.format("Error deleting user with UserNumber ${it}?:'null' | ${e.message}", it) }
}
