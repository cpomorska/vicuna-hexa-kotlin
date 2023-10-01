package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.dto.UuidResponse
import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.repository.UserRepository
import com.scprojekt.domain.model.user.service.UserStorageService
import jakarta.inject.Named

abstract class AbstractUserStorageService : UserStorageService {
    var userRepository : UserRepository? = null

    constructor(/* required for quarkus*/)
    constructor(@Named("UserJapRepository") userRepositoryInject: UserRepository) {
        userRepository = userRepositoryInject
    }

    override fun createUser(user: User): UuidResponse {
        return userRepository!!.createEntity(user)
    }

    override fun updateUser(user: User): UuidResponse {
        return userRepository!!.updateEntity(user)
    }

    override fun removeUser(user: User): UuidResponse {
        return userRepository!!.removeEntity(user)
    }
}
