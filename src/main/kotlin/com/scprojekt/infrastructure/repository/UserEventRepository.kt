package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.dto.response.UuidResponse
import com.scprojekt.domain.model.user.repository.UserEventRepository
import com.scprojekt.infrastructure.persistence.entity.UserEventEntity
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import java.util.*

@ApplicationScoped
@Transactional
class UserEventRepository @Inject constructor(private var em: EntityManager):
    UserEventRepository {
    override fun findByUUID(uuid: UUID?): UserEventEntity? {
        return try {
            val query: TypedQuery<UserEventEntity> = em.createQuery(" SELECT u from UserEventEntity u WHERE u.uuid =: uuid", UserEventEntity::class.java)
            query.setParameter("uuid", uuid).singleResult
        } catch (nre: NoResultException){
            null
        }
    }

    override fun findAllToRemove(): MutableList<UserEventEntity>? {
        return try {
            val query: TypedQuery<UserEventEntity> = em.createQuery(" SELECT u from UserEventEntity u", UserEventEntity::class.java)
            query.resultList
        } catch (nre: NoResultException){
            null
        }
    }

    override fun createEntity(entity: UserEventEntity): UuidResponse {
        em.merge(entity)
        em.flush()
        return UuidResponse(entity.uuid!!)
    }

    override fun removeEntity(entity: UserEventEntity): UuidResponse {
        val user = entity.uuid?.let { findByUUID(it) }
        em.remove(user)
        return UuidResponse(entity.uuid!!)
    }

    override fun updateEntity(entity: UserEventEntity): UuidResponse {
        em.merge(entity)
        return UuidResponse(entity.uuid!!)
    }
}