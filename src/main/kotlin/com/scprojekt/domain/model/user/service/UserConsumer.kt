package com.scprojekt.domain.model.user.service

import java.util.*

fun interface UserConsumer {
    fun receiveUserHandlingEvent(any: Any): UUID
}