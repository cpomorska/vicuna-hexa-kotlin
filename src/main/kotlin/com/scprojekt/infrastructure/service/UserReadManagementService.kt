package com.scprojekt.infrastructure.service

import com.scprojekt.domain.shared.service.BaseService
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserReadManagementService(): BaseService<UserEntity> {
}