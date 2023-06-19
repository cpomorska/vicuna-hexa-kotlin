package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.repository.UserRepository
import com.scprojekt.infrastructure.abstrct.AbstractUserReadOnlyService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class BaseUserReadOnlyService @Inject constructor(userRepository: UserRepository) : AbstractUserReadOnlyService(userRepository) {
}