package com.scprojekt.domain.model.user.messaging

import java.util.*

fun interface UserConsumer {
    fun receiveUserHandlingEvent(any: Any): UUID
}