package com.scprojekt.infrastructure.routes

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.infrastructure.processor.CamelConstants
import com.scprojekt.infrastructure.processor.JpaUrlProcessor
import com.scprojekt.infrastructure.processor.StringToUUidProcessor
import com.scprojekt.infrastructure.repository.BaseJpaUserRepository
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_CREATEUSER
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_DELETEUSER
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_FINDBYUUID
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DIRECT_SAVEINDATABASE
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.DRECT_MANAGEUSER
import com.scprojekt.infrastructure.routes.RouteConstants.Companion.MEDIATYPE_JSON
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestBindingMode
import java.util.*

@ApplicationScoped
class UserRestRoute : RouteBuilder() {

    @Inject
    private lateinit var baseUserRepository: BaseJpaUserRepository

    @Inject
    private lateinit var stringToUuidProcessor: StringToUUidProcessor

    @Inject
    private lateinit var prepareJpaUrlProcessor: JpaUrlProcessor

    override fun configure() {
        restConfiguration().bindingMode(RestBindingMode.json).inlineRoutes(true)
        findByUUIDInDatabase()
        storeInDatabase()
        createUserReadonlyRoute()
        createUserStoreRoute()
    }

    private fun createUserStoreRoute(){
        rest("/api/store/user")
            .post("create").consumes(MEDIATYPE_JSON).type(User::class.java).to(DIRECT_CREATEUSER).enableCORS(true)
            .post("manage").consumes(MEDIATYPE_JSON).type(User::class.java).to(DRECT_MANAGEUSER).enableCORS(true)
            .post("delete").consumes(MEDIATYPE_JSON).type(User::class.java).to(DIRECT_DELETEUSER).enableCORS(true)

        from(DIRECT_CREATEUSER)
            .routeId("infra.rest.createuser")
            .log("create user")
            .transacted()
            .bean(baseUserRepository, "createEntity(\${body})")
            .log(LoggingLevel.INFO, "Entity created")
            .end()

        from(DRECT_MANAGEUSER)
            .routeId("infra.rest.manageuser")
            .log("manage user")
            .transacted()
            .bean(baseUserRepository, "updateEntity(\${body})")
            .log(LoggingLevel.INFO, "Entity updated")
            .end()

        from(DIRECT_DELETEUSER)
            .routeId("infra.rest.removeuser")
            .log("deleting user \${body}")
            .transacted()
            .bean(baseUserRepository, "removeEntity(\${body})")
            .log(LoggingLevel.INFO, "Entity deleted")
            .end()
    }

    private fun createUserReadonlyRoute() {
        rest("/api/read/user")
            .get("uuid/{uuid}").consumes(MEDIATYPE_JSON).type(UUID::class.java).to("direct:byUUID").enableCORS(true)
            .get("bytype/{type}").consumes(MEDIATYPE_JSON).type(UserType::class.java).to("direct:byType").enableCORS(true)
            .get("byname/{name}").consumes(MEDIATYPE_JSON).type(String::class.java).to("direct:byName").enableCORS(true)
            .get("bydescr/{description}").consumes(MEDIATYPE_JSON).type(String::class.java).to("direct:byDescription").enableCORS(true)
            .enableCORS

        from("direct:byUUID")
            .routeId("infra.rest.byuuid")
            .log(LoggingLevel.INFO,"user by uuid")
            .process(stringToUuidProcessor)
            .bean(baseUserRepository, "findByUUID(\${body})")
            .marshal().json()
            .log(LoggingLevel.INFO, "user by \${body} found")
            .end()

        from("direct:byType")
            .routeId("infra.rest.bytype")
            .log(LoggingLevel.INFO,"user by id")
            .bean(baseUserRepository, "findByType(\${body}})")
            .log(LoggingLevel.INFO, "users by type found")
            .end()

        from("direct:byName")
            .routeId("infra.rest.byname")
            .log(LoggingLevel.INFO,"user by name")
            .bean(baseUserRepository, "findByName(\${body})")
            .log(LoggingLevel.INFO, "user by name found")
            .end()

        from("direct:byDescription")
            .routeId("infra.rest.bydescription")
            .log(LoggingLevel.INFO,"user by uuid")
            .bean(BaseJpaUserRepository::class.java, "findByDescription(\${body}})")
            .log(LoggingLevel.INFO, "users by description found")
            .end()
    }

    private fun storeInDatabase(){
        from(DIRECT_SAVEINDATABASE)
            .transacted()
            .process(prepareJpaUrlProcessor)
            .choice()
            .`when`(exchangeProperty(CamelConstants.JPA_URL).isNotNull())
            .to("jpa://" + exchangeProperty(CamelConstants.JPA_URL).toString())
            .otherwise()
            .log(LoggingLevel.INFO, CamelConstants.NODATABASE_URL)
            .end()
    }

    private fun findByUUIDInDatabase(){
        from(DIRECT_FINDBYUUID)
            .transacted()
            //.process(stringToUuidProcessor)
            .bean(BaseJpaUserRepository::class.java, "findByUUID(\${header.uuid})")
            .marshal().json()
            .log(LoggingLevel.INFO, "user by \${header.uuid} found")
            .end()
    }
}