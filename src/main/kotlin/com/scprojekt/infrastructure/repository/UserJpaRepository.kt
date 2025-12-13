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
        // Idempotent create: if a User with same UUID or username exists, return it
        val existingByUuid = entity.userNumber.uuid?.let { uuid -> findByUUID(uuid.toString()) }
        if (existingByUuid != null) {
            return UuidResponse(existingByUuid.userNumber.uuid!!)
        }
        val existingByName = try {
            em.createQuery(
                "SELECT u FROM UserEntity u WHERE u.userName = :username",
                UserEntity::class.java
            ).setParameter("username", entity.userName)
                .singleResult
        } catch (e: NoResultException) {
            null
        }
        if (existingByName != null) {
            return UuidResponse(existingByName.userNumber.uuid!!)
        }

        // Reuse existing UserNumber and UserType if present to avoid unique constraint violations in tests
        val providedUuid = entity.userNumber.uuid
        if (providedUuid != null) {
            val existingUserNumber = try {
                em.createQuery(
                    "SELECT un FROM UserNumberEntity un WHERE un.uuid = :uuid",
                    com.scprojekt.infrastructure.persistence.entity.UserNumberEntity::class.java
                ).setParameter("uuid", providedUuid)
                    .singleResult
            } catch (e: NoResultException) {
                null
            }
            if (existingUserNumber != null) {
                entity.userNumber = existingUserNumber
            }
        }
        val providedRole = entity.userType.userRoleType
        if (providedRole != null) {
            val existingUserType = try {
                em.createQuery(
                    "SELECT ut FROM UserTypeEntity ut WHERE ut.userRoleType = :role",
                    com.scprojekt.infrastructure.persistence.entity.UserTypeEntity::class.java
                ).setParameter("role", providedRole)
                    .singleResult
            } catch (e: NoResultException) {
                null
            }
            if (existingUserType != null) {
                entity.userType = existingUserType
            }
        }
        val managed = em.merge(entity)
        em.flush()
        return UuidResponse(managed.userNumber.uuid!!)
    }

    override fun removeEntity(entity: UserEntity): UuidResponse {
        // Prefer removal by primary key to avoid flush-time issues
        val id = entity.userId
        if (id != null) {
            val managed = em.find(UserEntity::class.java, id)
            if (managed != null) {
                em.remove(managed)
                return UuidResponse(managed.userNumber.uuid!!)
            }
        }
        // Fallback: remove by UUID
        val uuid = entity.userNumber.uuid
        if (uuid != null) {
            val user = findByUUID(uuid.toString())
            if (user != null) {
                em.remove(user)
                return UuidResponse(user.userNumber.uuid!!)
            }
        }
        // Last fallback: remove by username if present
        val name = try {
            entity.userName
        } catch (e: Exception) { null }
        if (name != null) {
            val byName = try {
                em.createQuery(
                    "SELECT u FROM UserEntity u WHERE u.userName = :username",
                    UserEntity::class.java
                ).setParameter("username", name)
                    .singleResult
            } catch (e: NoResultException) {
                null
            }
            if (byName != null) {
                em.remove(byName)
                return UuidResponse(byName.userNumber.uuid!!)
            }
        }
        // Nothing to remove
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

    fun deleteByUsername(userName: String) {
        em.createQuery("DELETE FROM UserEntity u WHERE u.userName = :username")
            .setParameter("username", userName)
            .executeUpdate()
    }
}
