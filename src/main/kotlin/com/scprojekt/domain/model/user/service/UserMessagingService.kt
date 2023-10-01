package com.scprojekt.domain.model.user.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.shared.service.BaseService
import java.util.*

interface UserMessagingService: BaseService<User> {
    fun registerNewUser(user: User): UUID
    fun manageExistingUser(user: User): UUID
    fun disableExistingUser(user: User): UUID
    fun deleteExistingUser(user: User): UUID
}