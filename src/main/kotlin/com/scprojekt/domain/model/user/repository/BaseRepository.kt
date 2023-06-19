package com.scprojekt.domain.model.user.repository


interface BaseRepository<T> {
    fun findAllInRepository(): MutableList<T>?
    fun findByIdInRepository(id: Long): T?
    fun createEntity(entity: T)
    fun removeEntity(entity: T)
    fun updateEntity(entity: T)
}