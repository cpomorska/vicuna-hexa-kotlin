package com.scprojekt.infrastructure.api

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.infrastructure.mapping.VicunaObjectMapper
import com.scprojekt.infrastructure.service.UserReadOnlyService
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import java.util.*

@Path("/opi/read/user")
@Produces(MediaType.APPLICATION_JSON)
class UserReadOnlyResource @Inject constructor(
    private val userReadOnlyService: UserReadOnlyService
) {
    @GET
    @Path("/uuid/{uuid}")
    fun getUserByUuid(@PathParam("uuid") uuid: UUID): User? {
        return userReadOnlyService.getUserByUuid(uuid.toString())
    }

    @GET
    @Path("/bytype/{usertype}")
    fun getUsersByType(@PathParam("usertype") usertype: String): List<User> {
        val type = VicunaObjectMapper.getInstance().readValue(usertype, UserType::class.java)
        return userReadOnlyService.findAllUsersByType(type)
    }

    @GET
    @Path("/byname/{name}")
    fun getUsersByName(@PathParam("name") name: String): List<User> {
        return userReadOnlyService.findAllUserByName(name)
    }

    @GET
    @Path("/bydescr/{description}")
    fun getUsersByDescription(@PathParam("description") description: String): List<User> {
        return userReadOnlyService.findAllUserByDescription(description)
    }
}