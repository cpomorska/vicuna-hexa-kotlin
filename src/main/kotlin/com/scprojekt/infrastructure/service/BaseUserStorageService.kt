package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.repository.UserRepository
import com.scprojekt.infrastructure.abstrct.AbstractUserStorageService
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

@ApplicationScoped
class BaseUserStorageService @Inject constructor(userRepository: UserRepository) : AbstractUserStorageService(userRepository) {
}