package com.scprojekt.infrastructure.abstrct

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.repository.UserRepository
import com.scprojekt.domain.model.user.service.UserStorageService
import java.util.*

abstract class AbstractUserStorageService : UserStorageService {
    var userRepository : UserRepository? = null

    constructor(/* required for quarkus*/)
    constructor(userRepositoryInject: UserRepository) {
        userRepository = userRepositoryInject
    }

    override fun createUser(user: User): UUID {
        userRepository!!.createEntity(user)
        return user.userNumber.uuid!!
    }

    override fun updateUser(user: User): UUID {
        userRepository!!.updateEntity(user)
        return user.userNumber.uuid!!
    }

    override fun removeUser(user: User): UUID {
        userRepository!!.removeEntity(user)
        return user.userNumber.uuid!!
    }
}
