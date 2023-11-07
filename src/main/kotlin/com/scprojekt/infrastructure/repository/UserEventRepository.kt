package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.dto.response.UuidResponse
import com.scprojekt.domain.model.user.entity.UserEvent
import com.scprojekt.domain.model.user.repository.UserEventRepository
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
    override fun findByUUID(uuid: UUID?): UserEvent? {
        return try {
            val query: TypedQuery<UserEvent> = em.createQuery(" SELECT u from UserEvent u WHERE u.uuid =: uuid", UserEvent::class.java)
            query.setParameter("uuid", uuid).singleResult
        } catch (nre: NoResultException){
            null
        }
    }

    override fun findAllToRemove(): MutableList<UserEvent>? {
        return try {
            val query: TypedQuery<UserEvent> = em.createQuery(" SELECT u from UserEvent u", UserEvent::class.java)
            query.resultList
        } catch (nre: NoResultException){
            null
        }
    }

    override fun createEntity(entity: UserEvent): UuidResponse {
        em.merge(entity)
        em.flush()
        return UuidResponse(entity.uuid!!)
    }

    override fun removeEntity(entity: UserEvent): UuidResponse {
        val user = entity.uuid?.let { findByUUID(it) }
        em.remove(user)
        return UuidResponse(entity.uuid!!)
    }

    override fun updateEntity(entity: UserEvent): UuidResponse {
        em.merge(entity)
        return UuidResponse(entity.uuid!!)
    }
}