package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.dto.response.UuidResponse
import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.domain.model.user.repository.UserRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.TypedQuery
import jakarta.transaction.Transactional

@ApplicationScoped
@Transactional
class UserJpaRepository @Inject constructor(private var em: EntityManager) : UserRepository {

    // Query constants for readability and reuse
    private companion object {
        const val FIND_BY_UUID_Q = "SELECT u FROM User u WHERE u.userNumber.uuid = :uuid"
        const val FIND_BY_TYPE_Q = "SELECT u FROM User u WHERE u.userType.userRoleType = :type"
        const val FIND_BY_NAME_Q = "SELECT u FROM User u WHERE u.userName = :username"
        const val FIND_BY_DESCRIPTION_Q = "SELECT u FROM User u WHERE u.userDescription = :userdescription"
        const val FIND_ALL_Q = "SELECT u FROM User u"
        const val UPDATE_USER_Q = "UPDATE User u SET u.userDescription = :userdescription, u.userName = :username WHERE u.userId = :userid"
    }

    override fun findByUUID(uuidString: String): User? {
        val parsedUuid = try {
            java.util.UUID.fromString(uuidString)
        } catch (ex: IllegalArgumentException) {
            return null
        }

        return try {
            val query: TypedQuery<User> = em.createQuery(FIND_BY_UUID_Q, User::class.java)
            query.setParameter("uuid", parsedUuid).singleResult
        } catch (nre: NoResultException) {
            null
        }
    }

    override fun findByType(userType: UserType): MutableList<User> {
        val query: TypedQuery<User> = em.createQuery(FIND_BY_TYPE_Q, User::class.java)
        query.setParameter("type", userType.userRoleType)
        return query.resultList
    }

    override fun findByName(userName: String): MutableList<User> {
        val query: TypedQuery<User> = em.createQuery(FIND_BY_NAME_Q, User::class.java)
        query.setParameter("username", userName)
        return query.resultList
    }

    override fun findByDescription(userDescription: String): MutableList<User> {
        val query: TypedQuery<User> = em.createQuery(FIND_BY_DESCRIPTION_Q, User::class.java)
        query.setParameter("userdescription", userDescription)
        return query.resultList
    }

    override fun createEntity(entity: User): UuidResponse {
        em.merge(entity)
        em.flush()
        return UuidResponse(entity.userNumber.uuid!!)
    }

    override fun removeEntity(entity: User): UuidResponse {
        val uuid = entity.userNumber.uuid ?: return UuidResponse(java.util.UUID.randomUUID()) // defensive fallback
        val user = findByUUID(uuid.toString())
        user?.let { em.remove(it) } // only remove if found
        return UuidResponse(uuid)
    }

    override fun updateEntity(entity: User): UuidResponse {
        val query = em.createQuery(UPDATE_USER_Q)
        query.setParameter("userid", entity.userId)
        query.setParameter("userdescription", entity.userDescription)
        query.setParameter("username", entity.userName)
        query.executeUpdate()

        return UuidResponse(entity.userNumber.uuid!!)
    }

    fun findAllToRemove(): MutableList<User>? {
        val query: TypedQuery<User> = em.createQuery(FIND_ALL_Q, User::class.java)
        return query.resultList
    }
}