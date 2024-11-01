package com.example

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.infrastructure.service.UserReadOnlyService
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.jwt.JsonWebToken
import java.util.*
import javax.annotation.security.RolesAllowed
import kotlin.collections.List

@Path("/opi/read/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class UserReadOnlyResource @Inject constructor(val jwt: JsonWebToken, val userReadOnlyService: UserReadOnlyService) {

    @GET
    @Path("/uuid/{uuid}")
    fun getByUUID(@PathParam("uuid") uuid: UUID): User? {
        return userReadOnlyService.getUserByUuid(uuid.toString()) as User
    }

    @GET
    @Path("/bytype/{type}")
    fun getByType(@PathParam("type") type:UserType): List<User?> {
        return userReadOnlyService.findAllUsersByType(type) as List<User>
    }

    @GET
    @Path("/byname/{name}")
    fun getByName(@PathParam("name") name: String): List<User?> {
        return userReadOnlyService.findAllUserByName(name) as List<User>
    }

    @GET
    @Path("/bydescr/{description}")
    fun getByDescription(@PathParam("description") description: String): List<User?>{
        return userReadOnlyService.findAllUserByDescription(description) as List<User?>
    }
}