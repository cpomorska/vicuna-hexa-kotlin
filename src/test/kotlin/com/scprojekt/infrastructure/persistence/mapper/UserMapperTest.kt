package com.scprojekt.infrastructure.persistence.mapper

import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserNumberEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.*

class UserMapperTest {

    private val userMapper = UserMapper()

    @Test
    fun shouldMapEntityToDomainCorrectly() {
        val entity = UserEntity().apply {
            userId = 1L
            userName = "testuser"
            userDescription = "Test Description"
            version = 5
            enabled = true
            userNumber = UserNumberEntity(UUID.randomUUID())
            userType = UserTypeEntity().apply {
                userRoleType = "ADMIN"
                userTypeDescription = "Admin User"
            }
        }

        val domainUser = userMapper.toDomain(entity)

        assertNotNull(domainUser)
        assertEquals(entity.userId, domainUser.id)
        assertEquals(entity.userName, domainUser.name)
        assertEquals(entity.userDescription, domainUser.description)
        assertEquals(entity.version.toLong(), domainUser.version)
        assertEquals(entity.enabled, domainUser.enabled)
        assertEquals(entity.userNumber.uuid, domainUser.number)
        assertEquals(entity.userType.userRoleType, domainUser.type.getRoleType())
    }

    @Test
    fun shouldMapDomainToEntityCorrectly() {
        val domainUser = com.scprojekt.domain.model.user.User(
            id = 1L,
            name = "testuser",
            description = "Test Description",
            type = com.scprojekt.domain.model.user.UserType.create("ADMIN", "Admin User"),
            version = 10L,
            enabled = true,
            number = UUID.randomUUID()
        )

        val entity = userMapper.toEntity(domainUser)

        assertNotNull(entity)
        assertEquals(domainUser.id, entity.userId)
        assertEquals(domainUser.name, entity.userName)
        assertEquals(domainUser.description, entity.userDescription)
        assertEquals(domainUser.version?.toInt(), entity.version)
        assertEquals(domainUser.enabled, entity.enabled)
        assertEquals(domainUser.number, entity.userNumber.uuid)
        assertEquals(domainUser.type.getRoleType(), entity.userType.userRoleType)
    }

    @Test
    fun shouldMapDomainToEntityWithContactInfoCorrectly() {
        val contactInfo1 = com.scprojekt.domain.model.user.value.ContactInfo(
            email = "test1@example.com",
            phone = "1234567890"
        )
        val contactInfo2 = com.scprojekt.domain.model.user.value.ContactInfo(
            email = "test2@example.com",
            phone = "9876543210"
        )

        val domainUser = com.scprojekt.domain.model.user.User(
            id = 2L,
            name = "userwithcontactinfo",
            description = "User with Contact Info",
            type = com.scprojekt.domain.model.user.UserType.create("USER", "Normal User"),
            version = 1L,
            enabled = true,
            number = UUID.randomUUID(),
            contactInfo = listOf(contactInfo1, contactInfo2)
        )

        val entity = userMapper.toEntity(domainUser)

        assertNotNull(entity)
        assertEquals(domainUser.id, entity.userId)
        assertEquals(domainUser.name, entity.userName)
        assertEquals(domainUser.description, entity.userDescription)
        assertEquals(domainUser.version?.toInt(), entity.version)
        assertEquals(domainUser.enabled, entity.enabled)
        assertEquals(domainUser.number, entity.userNumber.uuid)
        assertEquals(domainUser.type.getRoleType(), entity.userType.userRoleType)
        assertEquals(domainUser.contactInfo.size, entity.contactInfo.size)
        domainUser.contactInfo.forEach { domainContact ->
            val entityContact = entity.contactInfo.find { it.email == domainContact.email }
            assertNotNull(entityContact)
            assertEquals(domainContact.email, entityContact?.email)
            assertEquals(domainContact.phone, entityContact?.phone)
        }
    }
}
