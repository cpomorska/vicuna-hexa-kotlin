package com.scprojekt.application.api

import com.scprojekt.application.api.dto.ContactInfoDto
import com.scprojekt.application.api.dto.CreateUserDto
import com.scprojekt.application.api.dto.UpdateUserDto
import com.scprojekt.application.api.mapper.UserDtoMapper
import com.scprojekt.domain.model.user.UserType
import com.scprojekt.domain.model.user.service.DomainUserService
import com.scprojekt.domain.model.user.value.ContactInfo
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.util.UUID

@OpenAPIDefinition(
    info = Info(title = "Vicuna User API", version = "1.0.0"),
    security = [SecurityRequirement(name = "oidc")]
)
@SecurityScheme(
    name = "oidc",
    type = SecuritySchemeType.OPENIDCONNECT,
    openIdConnectUrl = "http://localhost:8180/realms/development/.well-known/openid_configuration"
)
/**
 * REST resource for User operations.
 * This resource uses the domain service and DTOs to expose User operations to clients.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "oidc")
class DomainUserResource {
    
    @Inject
    lateinit var userService: DomainUserService
    
    @Inject
    lateinit var userDtoMapper: UserDtoMapper
    @GET
    @Path("/{userId}")
    @Operation(summary = "Get a user by ID", description = "Returns the user with the specified ID")
    @SecurityRequirement(name = "oidc")
    fun getUser(@PathParam("userId") userId: UUID): Response {
        val userAggregate = userService.getUserByUuid(userId)
            ?: return Response.status(Response.Status.NOT_FOUND).build()

        val userDto = userDtoMapper.toDto(userAggregate.getUser())
        return Response.ok(userDto).build()
    }

    @GET
    @Operation(summary = "Find users by criteria", description = "Returns users matching the specified criteria")
    fun findUsers(
        @QueryParam("name") name: String?,
        @QueryParam("type") type: String?,
        @QueryParam("description") description: String?
    ): Response {
        val users = when {
            !name.isNullOrBlank() -> userService.findUsersByName(name)
            !type.isNullOrBlank() -> {
                val userType = UserType.create(type, "")
                userService.findUsersByType(userType)
            }
            !description.isNullOrBlank() -> userService.findUsersByDescription(description)
            else -> emptyList()
        }

        val userDtos = users.map { userDtoMapper.toDto(it.getUser()) }
        return Response.ok(userDtos).build()
    }

    @POST
    @Operation(summary = "Create a new user", description = "Creates a new user with the specified details")
    fun createUser(createUserDto: CreateUserDto): Response {
        val userType = UserType.create(
            roleType = createUserDto.userTypeRole,
            description = createUserDto.description
        )

        val (userAggregate, _) = userService.createUser(
            name = createUserDto.username,
            type = userType,
            description = createUserDto.description
        )

        // Add contact info if provided
        createUserDto.contactInfo.forEach { contactInfoDto ->
            userService.addContactInfo(
                userId = userAggregate.getNumber(),
                contactInfo = ContactInfo(
                    email = contactInfoDto.email,
                    phone = contactInfoDto.phone
                )
            )
        }

        val userDto = userDtoMapper.toDto(userAggregate.getUser())
        return Response.status(Response.Status.CREATED)
            .entity(userDto)
            .build()
    }

    @PUT
    @Path("/{userId}")
    @Operation(summary = "Update a user", description = "Updates the user with the specified ID")
    fun updateUser(
        @PathParam("userId") userId: UUID,
        updateUserDto: UpdateUserDto
    ): Response {
        // Validate that the path parameter matches the DTO
        if (updateUserDto.uuid != null && updateUserDto.uuid != userId) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Path parameter userId does not match the UUID in the request body")
                .build()
        }

        val userType = updateUserDto.userTypeRole?.let {
            UserType.create(
                roleType = it,
                description = updateUserDto.description ?: ""
            )
        }

        val result = userService.updateUser(
            userId = userId,
            name = updateUserDto.username,
            type = userType,
            description = updateUserDto.description
        ) ?: return Response.status(Response.Status.NOT_FOUND).build()

        // Update contact info if provided
        updateUserDto.contactInfo?.let { contactInfoDtos ->
            // Remove existing contact info
            val user = result.first.getUser()
            user.contactInfo.forEach { contactInfo ->
                userService.removeContactInfo(userId, contactInfo.email)
            }

            // Add new contact info
            contactInfoDtos.forEach { contactInfoDto ->
                userService.addContactInfo(
                    userId = userId,
                    contactInfo = ContactInfo(
                        email = contactInfoDto.email,
                        phone = contactInfoDto.phone
                    )
                )
            }
        }

        val userDto = userDtoMapper.toDto(result.first.getUser())
        return Response.ok(userDto).build()
    }

    @DELETE
    @Path("/{userId}")
    @Operation(summary = "Delete a user", description = "Deletes the user with the specified ID")
    fun deleteUser(@PathParam("userId") userId: UUID): Response {
        val event = userService.deleteUser(userId)
            ?: return Response.status(Response.Status.NOT_FOUND).build()

        return Response.ok(event).build()
    }

    @PUT
    @Path("/{userId}/disable")
    @Operation(summary = "Disable a user", description = "Disables the user with the specified ID")
    fun disableUser(@PathParam("userId") userId: UUID): Response {
        val result = userService.disableUser(userId)
            ?: return Response.status(Response.Status.NOT_FOUND).build()

        val userDto = userDtoMapper.toDto(result.first.getUser())
        return Response.ok(userDto).build()
    }

    @PUT
    @Path("/{userId}/enable")
    @Operation(summary = "Enable a user", description = "Enables the user with the specified ID")
    fun enableUser(@PathParam("userId") userId: UUID): Response {
        val result = userService.enableUser(userId)
            ?: return Response.status(Response.Status.NOT_FOUND).build()

        val userDto = userDtoMapper.toDto(result.first.getUser())
        return Response.ok(userDto).build()
    }

    @POST
    @Path("/{userId}/contact-info")
    @Operation(summary = "Add contact info", description = "Adds contact information to the user with the specified ID")
    fun addContactInfo(
        @PathParam("userId") userId: UUID,
        contactInfoDto: ContactInfoDto
    ): Response {
        val result = userService.addContactInfo(
            userId = userId,
            contactInfo = ContactInfo(
                email = contactInfoDto.email,
                phone = contactInfoDto.phone
            )
        ) ?: return Response.status(Response.Status.NOT_FOUND).build()

        val userDto = userDtoMapper.toDto(result.first.getUser())
        return Response.ok(userDto).build()
    }

    @DELETE
    @Path("/{userId}/contact-info/{email}")
    @Operation(summary = "Remove contact info", description = "Removes contact information from the user with the specified ID")
    fun removeContactInfo(
        @PathParam("userId") userId: UUID,
        @PathParam("email") email: String
    ): Response {
        val result = userService.removeContactInfo(userId, email)
            ?: return Response.status(Response.Status.NOT_FOUND).build()

        val userDto = userDtoMapper.toDto(result.first.getUser())
        return Response.ok(userDto).build()
    }
}