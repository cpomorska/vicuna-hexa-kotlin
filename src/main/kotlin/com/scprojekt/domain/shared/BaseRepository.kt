package com.scprojekt.domain.shared

import com.scprojekt.domain.model.user.dto.response.UuidResponse
import io.quarkus.hibernate.orm.panache.PanacheRepository


interface BaseRepository<T> : PanacheRepository<T> {
    fun removeEntity(entity: T): UuidResponse
    fun createEntity(entity: T): UuidResponse
    fun updateEntity(entity: T): UuidResponse
}