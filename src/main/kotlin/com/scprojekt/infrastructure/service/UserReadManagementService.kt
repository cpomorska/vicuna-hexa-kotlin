package com.scprojekt.infrastructure.service

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.shared.service.BaseService
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserReadManagementService(): BaseService<User> {
}