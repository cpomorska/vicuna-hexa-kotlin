package com.scprojekt.domain.model.user.service

import java.util.*

interface UserConsumer {
    fun recieveUserHandlingEvent(any: Any): UUID
}