package com.scprojekt.domain.model.user.repository

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.domain.shared.database.BaseRepository

interface UserIntegrationRepository : BaseRepository<User> {
    fun findByUUID(uid: String): User?
    fun findByType(userType: UserType): List<User>
    fun findByName(userName: String): List<User>
    fun findByDescription(userDescription: String): List<User>
}
