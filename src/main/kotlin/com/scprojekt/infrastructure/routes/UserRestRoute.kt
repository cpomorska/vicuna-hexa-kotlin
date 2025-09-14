package com.scprojekt.infrastructure.routes

import com.scprojekt.infrastructure.constants.Camel
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_FINDBYUUID
import com.scprojekt.infrastructure.constants.Routes.Companion.DIRECT_SAVEINDATABASE
import com.scprojekt.infrastructure.processor.JpaUrlProcessor
import com.scprojekt.infrastructure.processor.StringToUUidProcessor
import com.scprojekt.infrastructure.repository.UserJpaRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder


/**
 * Camel RouteBuilder for technical infrastructure routes only.
 * REST endpoints were moved to JAX-RS controllers to align with DDD layering.
 */
@ApplicationScoped
class UserRestRoute : RouteBuilder() {

    @Inject
    private lateinit var baseUserRepository: UserJpaRepository

    @Inject
    private lateinit var stringToUuidProcessor: StringToUUidProcessor

    @Inject
    private lateinit var prepareJpaUrlProcessor: JpaUrlProcessor

    override fun configure() {
        // REST endpoints have been migrated to JAX-RS controllers. Only internal routes remain.
        findByUUIDInDatabase()
        storeInDatabase()
    }

    private fun storeInDatabase(){
        from(DIRECT_SAVEINDATABASE)
            .transacted()
            .process(prepareJpaUrlProcessor)
            .choice()
            .`when`(exchangeProperty(Camel.JPA_URL).isNotNull())
            .to("jpa://" + exchangeProperty(Camel.JPA_URL).toString())
            .otherwise()
            .log(LoggingLevel.INFO, Camel.NODATABASE_URL)
            .end()
    }

    private fun findByUUIDInDatabase(){
        from(DIRECT_FINDBYUUID)
            .transacted()
            .process(stringToUuidProcessor)
            .bean(UserJpaRepository::class.java, "findByUUID(\${header.uuid})")
            .marshal().json()
            .log(LoggingLevel.INFO, "user by \${header.uuid} found")
            .end()
    }
}