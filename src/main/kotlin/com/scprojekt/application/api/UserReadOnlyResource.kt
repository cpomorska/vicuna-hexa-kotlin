package com.scprojekt.application.api

import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity
import com.scprojekt.infrastructure.service.UserReadOnlyService
import io.swagger.v3.oas.annotations.Operation
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.util.UUID

@Path("/opi/user/read")
@Produces(MediaType.APPLICATION_JSON)
class UserReadOnlyResource @Inject constructor(
    private val userReadOnlyService: UserReadOnlyService
) {
    @GET
    @Path("/uuid/{uuid}")
    @Operation(summary = "Retrieve a user by ID", description = "Returns the user with the specified ID.")
    fun getUserByUuid(@PathParam("uuid") uuid: UUID): UserEntity? {
        return userReadOnlyService.getUserByUuid(uuid.toString())
    }

    @GET
    @Path("/bytype")
    @Operation(
        summary = "Retrieve users by type",
        description = "Returns users matching the specified type criteria."
    )
    fun getUsersByType(@QueryParam("roleType") roleType: String?): List<UserEntity> {
        if (roleType.isNullOrBlank()) {
            throw WebApplicationException("roleType parameter is required", Response.Status.BAD_REQUEST)
        }

        val userTypeFilter = UserTypeEntity().apply {
            userRoleType = roleType
        }

        return userReadOnlyService.findAllUsersByType(userTypeFilter)
    }

    @GET
    @Path("/byname/{name}")
    fun getUsersByName(@PathParam("name") name: String): List<UserEntity> {
        return userReadOnlyService.findAllUserByName(name)
    }

    @GET
    @Path("/bydescr/{description}")
    fun getUsersByDescription(@PathParam("description") description: String): List<UserEntity> {
        return userReadOnlyService.findAllUserByDescription(description)
    }
}