package com.scprojekt.domain.model.user.service

import com.scprojekt.domain.model.user.entity.User
import java.util.*

interface UserStorageService {
    fun createUser(user: User): UUID
    fun updateUser(user: User): UUID
    fun removeUser(user: User): UUID
}
