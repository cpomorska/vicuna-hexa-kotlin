package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.dto.UuidResponse
import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.domain.model.user.exception.UserCreationException
import com.scprojekt.domain.model.user.exception.UserUpdateException
import com.scprojekt.domain.model.user.repository.UserIntegrationRepository
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_DELETEINDATABASE
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_FINDALL
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_FINDBYDESCRIPTION
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_FINDBYID
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_FINDBYNAME
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_FINDBYTYPE
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_FINDBYUUID
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_SAVEINDATABASE
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_UPDATEINDATABASE
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.apache.camel.ProducerTemplate
import java.util.concurrent.CompletableFuture

@ApplicationScoped
class UserCamelRepository : UserIntegrationRepository {

    @Inject
    private lateinit var camelProducer: ProducerTemplate

    override fun findByUUID(uid: String): User{
        val user: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDBYUUID, uid)
        return user?.get() as User
    }

    override fun findByType(userType: UserType): List<User> {
        val userList: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDBYTYPE, userType)
        return userList!!.get().let { it as List<User> }
    }

    override fun findByName(userName: String): List<User> {
        val userList: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDBYNAME, userName)
        return userList!!.get().let { it as List<User> }
    }

    override fun findByDescription(userDescription: String): List<User> {
        val userList: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDBYDESCRIPTION, userDescription)
        return userList!!.get().let { it as List<User> }
    }

    fun findAllInRepository(): List<User>? {
        val userList: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDALL,null)
        return userList!!.get().let { it as List<User> }
    }

    fun findByIdInRepository(id: Long): User? {
        val user: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(DIRECT_FINDBYID, id)
        return user!!.get() as User?
    }

    @Transactional
    override fun createEntity(entity: User): UuidResponse {
       val result: CompletableFuture<Any> = camelProducer.asyncSendBody(DIRECT_SAVEINDATABASE, entity)
        result.let { if (it.isCompletedExceptionally) throw UserCreationException(Throwable("Error"), "User not created") }
        return UuidResponse(entity.userNumber.uuid!!)
    }

    @Transactional
    override fun removeEntity(entity: User): UuidResponse {
        camelProducer.sendBody(DIRECT_DELETEINDATABASE, entity)
        return UuidResponse(entity.userNumber.uuid!!)
    }

    @Transactional
    override fun updateEntity(entity: User): UuidResponse {
        val result: CompletableFuture<Any> = camelProducer.asyncSendBody(DIRECT_UPDATEINDATABASE, entity)
        result.let { if (it.isCompletedExceptionally) throw UserUpdateException(Throwable("Error"), "User not updated") }
        return UuidResponse(entity.userNumber.uuid!!)
    }
}