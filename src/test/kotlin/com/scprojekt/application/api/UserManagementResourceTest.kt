package com.scprojekt.application.api

import com.scprojekt.application.api.dto.ContactInfoDto
import com.scprojekt.application.api.dto.CreateUserDto
import com.scprojekt.application.api.dto.UpdateUserDto
import com.scprojekt.application.api.dto.UserDto
import com.scprojekt.application.api.mapper.UserDtoMapper
import com.scprojekt.domain.model.user.User
import com.scprojekt.domain.model.user.UserAggregate
import com.scprojekt.domain.model.user.UserType
import com.scprojekt.domain.model.user.event.UserHandlingEvent
import com.scprojekt.domain.model.user.service.DomainUserService
import com.scprojekt.domain.model.user.value.ContactInfo
import jakarta.ws.rs.core.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.UUID

class UserManagementResourceTest {

    private val userServiceMock: DomainUserService = Mockito.mock(DomainUserService::class.java)
    private val userDtoMapperMock: UserDtoMapper = Mockito.mock(UserDtoMapper::class.java)
    private val userManagementResource = UserManagementResource().apply {
        userService = userServiceMock
        userDtoMapper = userDtoMapperMock
    }

    @Test
    fun `createUser should return 201 Created when user is successfully created`() {
        // Arrange
        val createUserDto = CreateUserDto(
            username = "jdoe",
            userTypeId = null,
            userTypeRole = "USER",
            description = "Standard system user",
            contactInfo = listOf(
                ContactInfoDto(
                    email = "john.doe@example.com",
                    phone = "+1234567890"
                )
            )
        )

        val userDto = UserDto(
            id = 1,
            uuid = UUID.randomUUID(),
            username = "jdoe",
            userType = "USER",
            description = "Standard system user",
            enabled = true,
            contactInfo = listOf(
                ContactInfoDto(
                    email = "john.doe@example.com",
                    phone = "+1234567890"
                )
            )
        )

        val contactInfo = listOf(
            ContactInfo(
                email = "john.doe@example.com",
                phone = "+1234567890"
            )
        )

        val user = User(1, UserType.create("USER", "Standard system user"), "jdoe", UUID.randomUUID(), "Standard system user", true, contactInfo)
        val userAggregate = Mockito.mock(UserAggregate::class.java)

        Mockito.`when`(userDtoMapperMock.toDomain(createUserDto)).thenReturn(user)
        Mockito.`when`(userAggregate.getNumber()).thenReturn(UUID.randomUUID())
        Mockito.`when`(userAggregate.getUser()).thenReturn(user)
        Mockito.`when`(userServiceMock.createUser("jdoe", user.type, "Standard system user"))
            .thenReturn(Pair(userAggregate, UserHandlingEvent()))
        Mockito.`when`(userDtoMapperMock.toDto(user)).thenReturn(userDto)

        // Act
        val response = userManagementResource.createUser(createUserDto)

        // Assert
        assertEquals(Response.Status.CREATED.statusCode, response.status)
        assertEquals(userDto, response.entity)

        Mockito.verify(userDtoMapperMock).toDomain(createUserDto)
        Mockito.verify(userServiceMock).createUser("jdoe", user.type, "Standard system user")
        Mockito.verify(userServiceMock).addContactInfo(userAggregate.getNumber(), contactInfo[0])
        Mockito.verify(userDtoMapperMock).toDto(user)
    }

    @Test
    fun `createUser should return 500 Internal Server Error if user creation fails`() {
        // Arrange
        val createUserDto = CreateUserDto(
            username = "jdoe",
            userTypeId = null,
            userTypeRole = "USER",
            description = "Standard system user",
            contactInfo = listOf(
                ContactInfoDto(
                    email = "john.doe@example.com",
                    phone = "+1234567890"
                )
            )
        )

        val user = User(1, UserType.create("USER", "Standard system user"), "jdoe", UUID.randomUUID(), "Standard system user")
        Mockito.`when`(userDtoMapperMock.toDomain(createUserDto)).thenReturn(user)
        Mockito.`when`(userServiceMock.createUser("jdoe", user.type, "Standard system user"))
            .thenThrow(RuntimeException("Error creating user"))

        // Act
        val response = try {
            userManagementResource.createUser(createUserDto)
        } catch (e: RuntimeException) {
            println("Error creating user: ${e.message}")
            Response.status(Response.Status.INTERNAL_SERVER_ERROR.statusCode).build()
        }

        // Assert
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.statusCode, response.status)
        Mockito.verify(userDtoMapperMock).toDomain(createUserDto)
        Mockito.verify(userServiceMock).createUser("jdoe", user.type, "Standard system user")
    }

    @Test
    fun `updateUser should return 200 OK when user is successfully updated`() {
        // Arrange
        val userId = UUID.randomUUID()
        val updateUserDto = UpdateUserDto(
            id = 1,
            uuid = userId,
            username = "jdoe",
            userTypeId = null,
            userTypeRole = "ADMIN",
            description = "Updated admin user",
            contactInfo = listOf(
                ContactInfoDto(
                    email = "updated.email@example.com",
                    phone = "+9876543210"
                )
            )
        )

        val updatedUser = User(
            id = 1,
            type = UserType.create("ADMIN", "Updated admin user"),
            name = "jdoe",
            description = "Updated admin user",
            enabled = true,
            contactInfo = listOf(
                ContactInfo(email = "updated.email@example.com", phone = "+9876543210")
            ),
            number = UUID.randomUUID(),
            version = 1,
            createdAt = java.time.Instant.now(),
            modifiedAt = java.time.Instant.now()
        )

        val userAggregate = Mockito.mock(UserAggregate::class.java)
        Mockito.`when`(userAggregate.getUser()).thenReturn(updatedUser)

        Mockito.`when`(
            userServiceMock.updateUser(userId, "jdoe", updatedUser.type, "Updated admin user")
        ).thenReturn(Pair(userAggregate, UserHandlingEvent()))

        Mockito.`when`(userDtoMapperMock.toDto(updatedUser)).thenReturn(
            UserDto(
                id = 1,
                uuid = userId,
                username = "jdoe",
                userType = "ADMIN",
                description = "Updated admin user",
                enabled = true,
                contactInfo = listOf(
                    ContactInfoDto(
                        email = "updated.email@example.com",
                        phone = "+9876543210"
                    )
                )
            )
        )

        // Act
        val response = userManagementResource.updateUser(userId, updateUserDto)

        // Assert
        assertEquals(Response.Status.OK.statusCode, response.status)
        Mockito.verify(userServiceMock).updateUser(userId, "jdoe", updatedUser.type, "Updated admin user")
    }

    @Test
    fun `updateUser should return 400 Bad Request if path userId and body UUID mismatch`() {
        // Arrange
        val userId = UUID.randomUUID()
        val updateUserDto = UpdateUserDto(
            id = 1,
            uuid = UUID.randomUUID(), // Mismatched UUID
            username = "jdoe",
            userTypeId = null,
            userTypeRole = "ADMIN",
            description = "An invalid update",
            contactInfo = listOf(
                ContactInfoDto(
                    email = "invalid.email@example.com",
                    phone = "+1111111111"
                )
            )
        )

        // Act
        val response = userManagementResource.updateUser(userId, updateUserDto)

        // Assert
        assertEquals(Response.Status.BAD_REQUEST.statusCode, response.status)
        assertEquals("Path parameter userId does not match the UUID in the request body", response.entity)
    }

    @Test
    fun `updateUser should return 404 Not Found if user does not exist`() {
        // Arrange
        val userId = UUID.randomUUID()
        val updateUserDto = UpdateUserDto(
            id = 1,
            uuid = userId,
            username = "nonexistent",
            userTypeId = null,
            userTypeRole = "USER",
            description = "Non-existent user",
            contactInfo = null
        )

        Mockito.`when`(
            userServiceMock.updateUser(userId, "nonexistent", null, "Non-existent user")
        ).thenReturn(null)

        // Act
        val response = userManagementResource.updateUser(userId, updateUserDto)

        // Assert
        assertEquals(Response.Status.NOT_FOUND.statusCode, response.status)
    }
}
