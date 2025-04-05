package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.domain.model.user.repository.UserRepository
import com.scprojekt.domain.model.user.service.UserReadOnlyService
import jakarta.inject.Named

abstract class AbstractUserReadOnlyService : UserReadOnlyService {
    var userRepository : UserRepository? = null

    constructor(/* required for quarkus*/)
    constructor(@Named("UserJpaRepository") userRepositoryInject: UserRepository) {
        userRepository = userRepositoryInject
    }

    override fun getUserByUuid(userUuid: String): User? {
        return userRepository?.findByUUID(userUuid)
    }

    override fun findAllUsersByType(userType: UserType): List<User> {
        return userRepository!!.findByType(userType)
    }

    override fun findAllUserByName(userName: String): List<User> {
        return userRepository!!.findByName(userName)
    }

    override fun findAllUserByDescription(userDescription: String): List<User> {
        return userRepository!!.findByDescription(userDescription)
    }
}
