package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.infrastructure.processor.CamelConstants
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.apache.camel.ProducerTemplate
import java.util.concurrent.CompletableFuture

@ApplicationScoped
class BaseCamelRepository {

    @Inject
    private lateinit var camelProducer: ProducerTemplate

    fun findByUUID(uid: String): User?{
        val user: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(CamelConstants.DIRECT_FINDBYUUID, uid)
        return user?.get() as User?
    }

    fun findByType(userType: UserType): List<User>? {
        val user: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(CamelConstants.DIRECT_FINDBYTYPE, userType)
        return user?.get() as List<User>?
    }

    fun findByName(userName: String): List<User> {
        val user: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(CamelConstants.DIRECT_FINDBYNAME, userName)
        return user?.get() as List<User>
    }

    fun findByDescription(userDescription: String): List<User> {
        val user: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(CamelConstants.DIRECT_FINDBYDESCRIPTION, userDescription)
        return user?.get() as List<User>
    }

    fun findAllInRepository(): List<User>? {
        val user: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(CamelConstants.DIRECT_FINDALL,null)
        return user?.get() as List<User>?
    }

    fun findByIdInRepository(id: Long): User? {
        val user: CompletableFuture<Any>? =  camelProducer.asyncRequestBody(CamelConstants.DIRECT_FINDBYID, id)
        return user?.get() as User?
    }

    @Transactional
    fun createEntity(entity: User) {
        camelProducer.sendBody(CamelConstants.DIRECT_SAVEINDATABASE, entity)
    }

    @Transactional
    fun removeEntity(entity: User) {
        camelProducer.sendBody(CamelConstants.DIRECT_DELETEINDATABASE, entity)
    }

    @Transactional
    fun updateEntity(entity: User) {
        camelProducer.sendBody(CamelConstants.DIRECT_UPDATEINDATABASE, entity)
    }
}