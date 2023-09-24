package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.domain.model.user.repository.UserRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import java.util.*

@ApplicationScoped
@Transactional
class UserJpaRepository @Inject constructor(private var em: EntityManager) : UserRepository {

    override fun findByUUID(uid: String): User? {
        val uuid = UUID.fromString(uid)
        return try {
            val query: TypedQuery<User> = em.createQuery(" SELECT u from User u WHERE u.userNumber.uuid =: uuid", User::class.java)
            query.setParameter("uuid", uuid).singleResult
        } catch (nre: NoResultException){
            null
        }
    }

    override fun findByType(userType: UserType): MutableList<User> {
        val query: TypedQuery<User> =
            em.createQuery(" SELECT u from User u WHERE u.userType =: type", User::class.java)
        query.setParameter("type", userType.userRoleType)
        return query.resultList
    }

    override fun findByName(userName: String): MutableList<User> {
        val query: TypedQuery<User> =
            em.createQuery(" SELECT u from User u WHERE u.userName =: username", User::class.java)
        query.setParameter("username", userName)
        return query.resultList
    }

    override fun findByDescription(userDescription: String): MutableList<User> {
        val query: TypedQuery<User> =
            em.createQuery(" SELECT u from User u WHERE u.userDescription =: userdescription", User::class.java)
        query.setParameter("userdescription", userDescription)
        return query.resultList
    }

    override fun createEntity(entity: User): UUID {
        em.merge(entity)
        em.flush()
        return entity.userNumber.uuid!!
    }

    override fun removeEntity(entity: User): UUID {
        val user = entity.userNumber.uuid?.let { findByUUID(it.toString()) }
        em.remove(user)
        return entity.userNumber.uuid!!
    }

    override fun updateEntity(entity: User): UUID {
        em.merge(entity)
        return entity.userNumber.uuid!!
    }

    fun findAllToRemove(): MutableList<User>? {
        val query: TypedQuery<User> =
            em.createQuery(" SELECT u from User u", User::class.java)
        return query.resultList
    }
}
