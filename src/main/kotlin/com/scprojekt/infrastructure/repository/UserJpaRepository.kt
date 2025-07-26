package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.dto.response.UuidResponse
import com.scprojekt.domain.model.user.repository.UserRepository
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity
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

    override fun findByUUID(uid: String): UserEntity? {
        val uuid = UUID.fromString(uid)
        return try {
            val query: TypedQuery<UserEntity> =
                em.createQuery(" SELECT u from UserEntity u WHERE u.userNumber.uuid =: uuid", UserEntity::class.java)
            query.setParameter("uuid", UUID.fromString(uid)).singleResult
        } catch (nre: NoResultException) {
            null
        }
    }

    override fun findByType(userTypeEntity: UserTypeEntity): MutableList<UserEntity> {
        val query: TypedQuery<UserEntity> =
            em.createQuery(" SELECT u from UserEntity u WHERE u.userType.userRoleType =: type", UserEntity::class.java)
        query.setParameter("type", userTypeEntity.userRoleType)
        return query.resultList
    }

    override fun findByName(userName: String): MutableList<UserEntity> {
        val query: TypedQuery<UserEntity> =
            em.createQuery(" SELECT u from UserEntity u WHERE u.userName =: username", UserEntity::class.java)
        query.setParameter("username", userName)
        return query.resultList
    }

    override fun findByDescription(userDescription: String): MutableList<UserEntity> {
        val query: TypedQuery<UserEntity> =
            em.createQuery(" SELECT u from UserEntity u WHERE u.userDescription =: userdescription", UserEntity::class.java)
        query.setParameter("userdescription", userDescription)
        return query.resultList
    }

    override fun createEntity(entity: UserEntity): UuidResponse {
        em.merge(entity)
        em.flush()
        return UuidResponse(entity.userNumber.uuid!!)
    }

    override fun removeEntity(entity: UserEntity): UuidResponse {
        val user = entity.userNumber.uuid?.let { findByUUID(it.toString()) }
        if (user != null) em.remove(user)
        return UuidResponse(entity.userNumber.uuid!!)
    }

    override fun updateEntity(entity: UserEntity): UuidResponse {
        val query = em.createQuery(
            " UPDATE UserEntity u set u.userDescription =: userdescription, u.userName =: username WHERE u.userId =: userid"
        )
        query.setParameter("userid", entity.userId)
        query.setParameter("userdescription", entity.userDescription)
        query.setParameter("username", entity.userName)
        query.executeUpdate()

        return UuidResponse(entity.userNumber.uuid!!)
    }

    fun findAllToRemove(): MutableList<UserEntity>? {
        val query: TypedQuery<UserEntity> = em.createQuery(" SELECT u from UserEntity u", UserEntity::class.java)
        return query.resultList
    }
}
