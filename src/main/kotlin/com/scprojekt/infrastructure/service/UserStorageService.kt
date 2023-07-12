package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.repository.UserRepository
import com.scprojekt.infrastructure.abstrct.AbstractUserStorageService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
 * Stores a User or a list of Users in Backend
 * Implementation of @see{AbstractUserStorageService}
 */
@ApplicationScoped
class UserStorageService @Inject constructor(userRepository: UserRepository) : AbstractUserStorageService(userRepository) {
}