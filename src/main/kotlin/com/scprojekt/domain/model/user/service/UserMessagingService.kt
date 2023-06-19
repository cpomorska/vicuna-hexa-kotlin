package com.scprojekt.domain.model.user.service

import com.scprojekt.domain.model.user.entity.User
import java.util.*

interface UserMessagingService {
    fun registerNewUser(user: User): UUID
    fun manageExistingUser(user: User): UUID
    fun disableExistingUser(user: User): UUID
    fun deleteExistingUser(user: User): UUID
}