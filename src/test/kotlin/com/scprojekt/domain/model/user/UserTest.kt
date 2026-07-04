package com.scprojekt.domain.model.user

import com.scprojekt.domain.model.user.value.ContactInfo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import java.time.Instant

internal class UserTest {

    @Test
    fun `test create user with valid name and type`() {
        val userType = mock<UserType>()
        val user = User.create("JohnDoe", userType, "Regular user")

        assertNotNull(user)
        assertEquals("JohnDoe", user.name)
        assertEquals(userType, user.type)
        assertEquals("Regular user", user.description)
        assertNotNull(user.number)
        assertTrue(user.enabled)
    }

    @Test
    fun `test create user with blank name throws exception`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            User.create("", mock(), "Description")
        }
        assertEquals("Username cannot be blank", exception.message)
    }

    @Test
    fun `test create user with short name throws exception`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            User.create("Jo", mock(), "Description")
        }
        assertEquals("Username must be at least 3 characters", exception.message)
    }

    @Test
    fun `test change user type`() {
        val userType = mock<UserType>()
        val newType = mock<UserType>()
        val user = User.create("JohnDoe", userType, "Description")

        val updatedUser = user.changeUserType(newType)

        assertEquals(newType, updatedUser.type)
        assertNotEquals(user.type, updatedUser.type)
    }

    @Test
    fun `test change user name with valid name`() {
        val user = User.create("JohnDoe", mock(), "Description")

        val updatedUser = user.changeName("JaneDoe")

        assertEquals("JaneDoe", updatedUser.name)
        assertNotEquals(user.name, updatedUser.name)
    }

    @Test
    fun `test change user name with blank name throws exception`() {
        val user = User.create("JohnDoe", mock(), "Description")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            user.changeName("")
        }
        assertEquals("Username cannot be blank", exception.message)
    }

    @Test
    fun `test change user description`() {
        val user = User.create("JohnDoe", mock(), "Description")

        val updatedUser = user.changeDescription("New Description")

        assertEquals("New Description", updatedUser.description)
        assertNotEquals(user.description, updatedUser.description)
    }

    @Test
    fun `test disable user`() {
        val user = User.create("JohnDoe", mock(), "Description")

        val disabledUser = user.disable()

        assertFalse(disabledUser.enabled)
        assertNotEquals(user.enabled, disabledUser.enabled)
    }

    @Test
    fun `test enable user`() {
        val user = User.create("JohnDoe", mock(), "Description").disable()

        val enabledUser = user.enable()

        assertTrue(enabledUser.enabled)
        assertNotEquals(user.enabled, enabledUser.enabled)
    }

    @Test
    fun `test add contact info`() {
        val user = User.create("JohnDoe", mock(), "Description")
        val contactInfo = ContactInfo("johndoe@example.com", "+1234567890")

        val updatedUser = user.addContactInfo(contactInfo)

        assertTrue(updatedUser.contactInfo.contains(contactInfo))
        assertTrue(updatedUser.contactInfo.size > user.contactInfo.size)
    }

    @Test
    fun `test remove contact info`() {
        val contactInfo = ContactInfo("johndoe@example.com", "+1234567890")
        val user = User.create("JohnDoe", mock(), "Description")
            .addContactInfo(contactInfo)

        val updatedUser = user.removeContactInfo("johndoe@example.com")

        assertFalse(updatedUser.contactInfo.contains(contactInfo))
        assertTrue(updatedUser.contactInfo.size < user.contactInfo.size)
    }

    @Test
    fun `test set persistence data`() {
        val user = User.create("JohnDoe", mock(), "Description")

        val updatedUser = user.withPersistenceData(
            id = 1L,
            version = 1L,
            createdAt = Instant.now(),
            modifiedAt = Instant.now()
        )

        assertEquals(1L, updatedUser.id)
        assertEquals(1L, updatedUser.version)
        assertNotNull(updatedUser.createdAt)
        assertNotNull(updatedUser.modifiedAt)
    }
}