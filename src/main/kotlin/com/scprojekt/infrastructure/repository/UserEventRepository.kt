package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.dto.UuidResponse
import com.scprojekt.domain.model.user.entity.UserEventStore
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
    override fun findByUUID(uuid: UUID?): UserEventStore? {
        return try {
            val query: TypedQuery<UserEventStore> = em.createQuery(" SELECT u from UserEventStore u WHERE u.uuid =: uuid", UserEventStore::class.java)
            query.setParameter("uuid", uuid).singleResult
        } catch (nre: NoResultException){
            null
        }
    }

    override fun findAllToRemove(): MutableList<UserEventStore>? {
        return try {
            val query: TypedQuery<UserEventStore> = em.createQuery(" SELECT u from UserEventStore u", UserEventStore::class.java)
            query.resultList
        } catch (nre: NoResultException){
            null
        }
    }

    override fun createEntity(entity: UserEventStore): UuidResponse {
        em.merge(entity)
        em.flush()
        return UuidResponse(entity.uuid!!)
    }

    override fun removeEntity(entity: UserEventStore): UuidResponse {
        val user = entity.uuid?.let { findByUUID(it) }
        em.remove(user)
        return UuidResponse(entity.uuid!!)
    }

    override fun updateEntity(entity: UserEventStore): UuidResponse {
        em.merge(entity)
        return UuidResponse(entity.uuid!!)
    }
}