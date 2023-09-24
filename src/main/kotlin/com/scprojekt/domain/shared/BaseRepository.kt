package com.scprojekt.mimetidae.domain.shared

import java.util.*


interface BaseRepository<T>  {
    fun removeEntity(type: T): UUID
    fun createEntity(type: T): UUID
    fun updateEntity(type: T): UUID
}