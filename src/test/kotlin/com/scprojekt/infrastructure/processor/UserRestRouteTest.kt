package com.scprojekt.infrastructure.processor

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserNumber
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.infrastructure.repository.BaseCamelRepository
import com.scprojekt.infrastructure.repository.BaseJpaUserRepository
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Consumer


private const val TESTROLE = "testrole"
private const val TESTUSER = "Testuser"
private const val USER_ID_TESTUSER_1 = 1L
private const val UUID_TESTUSER_1 = "586c2084-d545-4fac-b7d3-2319382df14f"

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
class UserRestRouteTest {

    private lateinit var testUser: User

    @Inject
    @field:Default
    lateinit var userCamelRepository: BaseCamelRepository

    @Inject
    @field: Default
    lateinit var userRepository: BaseJpaUserRepository

    @BeforeEach
    @Transactional
    fun setup() {
        testUser = createTestUser()

        val users: MutableList<User>? = userRepository.findAllInRepository()
        users?.forEach(Consumer { u: User ->
            userRepository.removeEntity(u)
        })
    }

    @Test
    fun ifAnUserNotExistInDatabaseItCanBeCreatedViaCreateEndpoint() {
        given()
            .header("Content-type", "application/json")
            .and()
            .body(testUser)
            .`when`()
            .post("/api/store/user/create")
            .then()
            .statusCode(200)
    }

    @Test
    fun ifAnUserExistInDatabaseItCanBeUpdatedViaManageEndpoint() {
        userCamelRepository.createEntity(testUser)
        val userFromRepo: User? = userRepository.findByUUID(UUID_TESTUSER_1)

        given()
            .header("Content-type", "application/json")
            .and()
            .body(userFromRepo)
            .`when`()
            .post("/api/store/user/manage")
            .then()
            .statusCode(200)
    }

    @Test
    fun ifAnUserExistInDatabaseItCanBeDeletedViaDeleteEndpoint() {
        userCamelRepository.createEntity(testUser)
        val userFromRepo: User? = userRepository.findByUUID(UUID_TESTUSER_1)

        given()
            .header("Content-type", "application/json")
            .and()
            .body(userFromRepo)
            .`when`()
            .post("/api/store/user/delete")
            .then()
            .statusCode(200)
    }

    private fun createTestUser(): User {
        val user = User()
        val userType = UserType()
        val userTypeList: MutableList<UserType> = ArrayList()
        val userNumber = UserNumber()
        userNumber.uuid = UUID.fromString(UUID_TESTUSER_1)
        userType.userTypeId = USER_ID_TESTUSER_1
        userType.userRoleType = TESTROLE
        userType.userTypeDescription = TESTUSER
        userTypeList.add(userType)
        user.userId = USER_ID_TESTUSER_1
        user.userName = TESTUSER
        user.userDescription = TESTUSER
        user.userNumber = userNumber
        user.userType = "UserType"
        return user
    }
}