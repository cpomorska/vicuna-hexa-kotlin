package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.dto.response.UuidResponse
import com.scprojekt.domain.model.user.repository.UserRepository
import com.scprojekt.domain.model.user.service.UserStorageService
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import jakarta.inject.Named

abstract class AbstractUserStorageService : UserStorageService {
    var userRepository : UserRepository? = null

    constructor(/* required for quarkus*/)
    constructor(@Named("UserJapRepository") userRepositoryInject: UserRepository) {
        userRepository = userRepositoryInject
    }

    override fun createUser(userEntity: UserEntity): UuidResponse {
        return userRepository!!.createEntity(userEntity)
    }

    override fun updateUser(userEntity: UserEntity): UuidResponse {
        return userRepository!!.updateEntity(userEntity)
    }

    override fun removeUser(userEntity: UserEntity): UuidResponse {
        return userRepository!!.removeEntity(userEntity)
    }
}
