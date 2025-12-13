package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.repository.UserRepository
import com.scprojekt.domain.model.user.service.UserReadOnlyService
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity
import jakarta.inject.Named

abstract class AbstractUserReadOnlyService : UserReadOnlyService {
    var userRepository : UserRepository? = null

    constructor(/* required for quarkus*/)
    constructor(@Named("UserJpaRepository") userRepositoryInject: UserRepository) {
        userRepository = userRepositoryInject
    }

    override fun getUserByUuid(userUuid: String): UserEntity? {
        return userRepository?.findByUUID(userUuid)
    }

    override fun findAllUsersByType(userTypeEntity: UserTypeEntity): List<UserEntity> {
        return userRepository!!.findByType(userTypeEntity)
    }

    override fun findAllUserByName(userName: String): List<UserEntity> {
        return userRepository!!.findByName(userName)
    }

    override fun findAllUserByDescription(userDescription: String): List<UserEntity> {
        return userRepository!!.findByDescription(userDescription)
    }
}
