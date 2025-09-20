package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.dto.response.UuidResponse
import com.scprojekt.domain.model.user.exception.UserCreationException
import com.scprojekt.domain.model.user.exception.UserUpdateException
import com.scprojekt.domain.model.user.repository.UserIntegrationRepository
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_DELETEINDATABASE
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_FINDALL
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_FINDBYDESCRIPTION
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_FINDBYID
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_FINDBYNAME
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_FINDBYTYPE
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_FINDBYUUID
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_SAVEINDATABASE
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_UPDATEINDATABASE
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.apache.camel.ProducerTemplate
import java.util.concurrent.CompletableFuture

/**
 * Camel-based implementation of the UserIntegrationRepository.
 * Uses ProducerTemplate to communicate with internal direct: routes defined in the infrastructure layer.
 */
@ApplicationScoped
class UserCamelRepository : UserIntegrationRepository {

    @Inject
    private lateinit var camelProducer: ProducerTemplate

    override fun findByUUID(uid: String): UserEntity {
        val user: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDBYUUID, uid)
        return user?.get() as UserEntity
    }

    override fun findByType(userTypeEntity: UserTypeEntity): List<UserEntity> {
        val userList: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDBYTYPE, userTypeEntity)
        return listOf(userList!!.get() as UserEntity)
    }

    override fun findByName(userName: String): List<UserEntity> {
        val userList: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDBYNAME, userName)
        return listOf(userList!!.get() as UserEntity)
    }

    override fun findByDescription(userDescription: String): List<UserEntity> {
        val userList: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDBYDESCRIPTION, userDescription)
        return listOf(userList!!.get() as UserEntity)
    }

    fun findAllInRepository(): List<UserEntity>? {
        val userList: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDALL,null)
        return listOf(userList!!.get() as UserEntity)
    }

    fun findByIdInRepository(id: Long): UserEntity? {
        val user: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDBYID, id)
        return user!!.get() as UserEntity?
    }

    @Transactional
    override fun createEntity(entity: UserEntity): UuidResponse {
       val result: CompletableFuture<Any> = camelProducer.asyncSendBody(DIRECT_SAVEINDATABASE, entity)
        result.let { if (it.isCompletedExceptionally) throw UserCreationException(Throwable("Error"), "User not created") }
        return UuidResponse(entity.userNumber.uuid!!)
    }

    @Transactional
    override fun removeEntity(entity: UserEntity): UuidResponse {
        camelProducer.sendBody(DIRECT_DELETEINDATABASE, entity)
        return UuidResponse(entity.userNumber.uuid!!)
    }

    @Transactional
    override fun updateEntity(entity: UserEntity): UuidResponse {
        val result: CompletableFuture<Any> = camelProducer.asyncSendBody(DIRECT_UPDATEINDATABASE, entity)
        result.let { if (it.isCompletedExceptionally) throw UserUpdateException(Throwable("Error"), "User not updated") }
        return UuidResponse(entity.userNumber.uuid!!)
    }
}