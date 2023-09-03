package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.entity.UserEventStore
import com.scprojekt.domain.model.user.repository.UserStoreRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import java.util.*

@ApplicationScoped
@Transactional
class UserEventStoreRepository @Inject constructor(private var em: EntityManager): UserStoreRepository {
    override fun findByUUID(uuid: UUID?): UserEventStore? {
        return try {
            val query: TypedQuery<UserEventStore> = em.createQuery(" SELECT u from UserEventStore u WHERE u.uuid =: uuid", UserEventStore::class.java)
            query.setParameter("uuid", uuid).singleResult
        } catch (nre: NoResultException){
            null
        }
    }

    override fun findAllInRepository(): MutableList<UserEventStore>? {
        val query: TypedQuery<UserEventStore> = em.createQuery(" SELECT u from UserEventStore u", UserEventStore::class.java)
        return query.resultList
    }

    override fun findByIdInRepository(id: Long): UserEventStore? {
        return em.find(UserEventStore::class.java, id)
    }

    override fun createEntity(entity: UserEventStore) {
        em.merge(entity)
        em.flush()
    }

    override fun removeEntity(entity: UserEventStore) {
        val user = entity.userEventStoreId?.let { findByIdInRepository(it) }
        em.remove(user)
    }

    override fun updateEntity(entity: UserEventStore) {
        em.merge(entity)
    }
}