package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.repository.UserRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

/**
 * Reads a User or a list of Users from Backend
 * Implementation of @see{AbstractUserReadOnlyService}
 */
@ApplicationScoped
class UserReadOnlyService @Inject constructor(userRepository: UserRepository) : AbstractUserReadOnlyService(userRepository) {
}