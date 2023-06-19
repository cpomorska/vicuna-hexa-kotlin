package com.scprojekt.domain.model.user.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType

interface UserReadOnlyService {
    fun getUserById(id: Long): User
    fun getUserByUuid(userUuid: String): User?
    fun findAllUsersByType(userType: UserType): List<User>
    fun findAllUserByName(userName: String): List<User>
    fun findAllUserByDescription(userDescription: String): List<User>
}
