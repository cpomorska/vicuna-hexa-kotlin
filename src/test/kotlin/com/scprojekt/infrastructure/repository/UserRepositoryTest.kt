package com.scprojekt.infrastructure.repository

import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.util.TESTUSER
import com.scprojekt.util.TestUtil.Companion.createTestUser
import com.scprojekt.util.USER_ID_TESTUSER_1
import com.scprojekt.util.UUID_TESTUSER_1
import com.scprojekt.util.UUID_TESTUSER_2
import io.quarkus.test.common.WithTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import java.util.function.Consumer

@Disabled
@QuarkusTest
@WithTestResource(H2DatabaseTestResource::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest  {

    @Inject
    lateinit var userRepository: UserJpaRepository

    @AfterEach
    @Transactional
    fun teardown() {
        val userEntities: MutableList<UserEntity>? = userRepository.findAllToRemove()
        userEntities?.forEach(Consumer { u: UserEntity -> userRepository.entityManager.remove(u)
        })
    }

    @Test
    @Transactional
    fun findByUUID() {
        val userEntity: UserEntity = createTestUser()
        userRepository.entityManager.merge(userEntity)
        val result: UserEntity? = userRepository.findByUUID(UUID_TESTUSER_1)

        assertThat(result?.userNumber!!.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
    }

    @Test
    fun findByType() {
    }

    @Test
    @Transactional
    fun findByName() {
        val userEntity: UserEntity = createTestUser()
        userRepository.createEntity(userEntity)
        val result: UserEntity = userRepository.findByName(TESTUSER).first()

        assertThat(result.userNumber.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result.userName).isEqualTo(TESTUSER)
    }

    @Test
    fun findByDescription() {
        val userEntity: UserEntity = createTestUser()
        userRepository.createEntity(userEntity)
        val result: UserEntity = userRepository.findByDescription(TESTUSER).first()

        assertThat(result.userNumber.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result.userDescription).isEqualTo(TESTUSER)
    }

    @Test
    fun findAllInRepository() {
        val userEntity: UserEntity = createTestUser()
        userRepository.createEntity(userEntity)
        val result: MutableList<UserEntity>? = userRepository.findAllToRemove()

        assertThat(result).isNotEmpty
        assertThat(result?.count()).isEqualTo(USER_ID_TESTUSER_1)
    }

    @Test
    fun findByIdInRepository() {
        val userEntity: UserEntity = createTestUser()
        userRepository.createEntity(userEntity)
        val testUserEntity: UserEntity? = userRepository.findAllToRemove()?.first()

        val result: UserEntity? = testUserEntity?.userNumber?.uuid?.let { userRepository.findByUUID(it.toString()) }
        assertThat(result?.userId).isEqualTo(testUserEntity?.userId)
        assertThat(result?.userNumber?.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_1))
        assertThat(result?.userName).isEqualTo(TESTUSER)
    }

    @Test
    @Transactional
    fun createEntity() {
        val newUserEntity: UserEntity = createTestUser()
        newUserEntity.userNumber.uuid = UUID.fromString(UUID_TESTUSER_2)

        userRepository.createEntity(newUserEntity)
        val result: UserEntity? = userRepository.findByUUID(UUID_TESTUSER_2)

        assertThat(result?.userNumber!!.uuid).isEqualTo(UUID.fromString(UUID_TESTUSER_2))
    }

    @Test
    @Transactional
    fun removeEntity() {
        val userEntity: UserEntity = createTestUser()
        userRepository.createEntity(userEntity)
        val testUserEntity: UserEntity = userRepository.findAllToRemove()?.first()!!
        userRepository.removeEntity(testUserEntity)

        val result: MutableList<UserEntity>? = userRepository.findAllToRemove()

        assertThat(result).isEmpty()
        assertThat(result?.count()).isZero()
    }

    @Test
    @Transactional
    fun updateEntity() {
        val newUserEntity: UserEntity = createTestUser()
        userRepository.createEntity(newUserEntity)
        val result = userRepository.findByUUID(UUID_TESTUSER_1)

        result?.userName = "Nanana"
        userRepository.updateEntity(result!!)
        val result1 = userRepository.findByName("Nanana")

        assertThat(result1.first().userName).isEqualTo("Nanana")
    }

}