package com.scprojekt.infrastructure.persistence.repository

import com.scprojekt.domain.model.user.UserAggregate
import com.scprojekt.domain.model.user.UserType
import com.scprojekt.domain.model.user.repository.DomainUserRepository
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.mapper.UserMapper
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional
import java.util.*

/**
 * Implementation of the DomainUserRepository interface.
 * This class is responsible for persisting and retrieving User aggregates.
 */
@ApplicationScoped
@Transactional
class DomainUserRepositoryImpl @Inject constructor(
    private val entityManager: EntityManager,
    private val userMapper: UserMapper
) : DomainUserRepository {

    override fun findByUUID(uid: UUID?): UserAggregate? {
        return try {
            val query: TypedQuery<UserEntity> =
                entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.userNumber.uuid = :uuid",
                    UserEntity::class.java
                )
            val entity = query.setParameter("uuid", uid).singleResult
            UserAggregate.fromUser(userMapper.toDomain(entity))
        } catch (nre: NoResultException) {
            null
        }
    }

    override fun findByType(userType: UserType): List<UserAggregate> {
        val query: TypedQuery<UserEntity> =
            entityManager.createQuery(
                "SELECT u FROM UserEntity u WHERE u.userType.userRoleType = :type",
                UserEntity::class.java
            )
        val entities = query.setParameter("type", userType.getRoleType()).resultList
        return entities.map { UserAggregate.fromUser(userMapper.toDomain(it)) }
    }

    override fun findByName(userName: String): List<UserAggregate> {
        val query: TypedQuery<UserEntity> =
            entityManager.createQuery(
                "SELECT u FROM UserEntity u WHERE u.userName = :username",
                UserEntity::class.java
            )
        val entities = query.setParameter("username", userName).resultList
        return entities.map { UserAggregate.fromUser(userMapper.toDomain(it)) }
    }

    override fun findByDescription(userDescription: String): List<UserAggregate> {
        val query: TypedQuery<UserEntity> =
            entityManager.createQuery(
                "SELECT u FROM UserEntity u WHERE u.userDescription = :userdescription",
                UserEntity::class.java
            )
        val entities = query.setParameter("userdescription", userDescription).resultList
        return entities.map { UserAggregate.fromUser(userMapper.toDomain(it)) }
    }

    override fun save(userAggregate: UserAggregate): UUID {
        val user = userAggregate.getUser()
        val entity = userMapper.toEntity(user)
        
        if (entity.userId == null) {
            entityManager.persist(entity)
        } else {
            entityManager.merge(entity)
        }
        
        entityManager.flush()
        return entity.userNumber.uuid!!
    }

    override fun delete(userAggregate: UserAggregate): UUID? {
        val user = userAggregate.getUser()
        val entity = userMapper.toEntity(user)
        
        val managedEntity = if (entity.userId != null) {
            entityManager.find(UserEntity::class.java, entity.userId)
        } else {
            val query: TypedQuery<UserEntity> =
                entityManager.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.userNumber.uuid = :uuid",
                    UserEntity::class.java
                )
            query.setParameter("uuid", user.number).singleResult
        }
        
        if (managedEntity != null) {
            entityManager.remove(managedEntity)
        }
        
        return user.number
    }
}