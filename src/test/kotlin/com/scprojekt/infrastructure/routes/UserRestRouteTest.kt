package com.scprojekt.infrastructure.routes

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.infrastructure.repository.BaseCamelRepository
import com.scprojekt.infrastructure.repository.BaseJpaUserRepository
import com.scprojekt.util.*
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
        testUser = UserTestUtil.createTestUser()

        val users: MutableList<User>? = userRepository.findAllInRepository()
        users?.forEach(Consumer { u: User ->
            userRepository.removeEntity(u)
        })
    }

    @Test
    fun ifUserNotExistsInDatabaseItWillBeCreatedViaCreateEndpoint() {
        given()
            .header(HEADER_CONTENT_TYPE, VALUE_APPLICATION_JSON)
            .and()
            .body(testUser)
            .`when`()
            .post(URI_CREATE)
            .then()
            .statusCode(200)
    }

    @Test
    fun ifUserExistsInDatabaseItWillBeUpdatedViaManageEndpoint() {
        userCamelRepository.createEntity(testUser)
        val userFromRepo: User? = userRepository.findByUUID(UUID_TESTUSER_1)

        given()
            .header(HEADER_CONTENT_TYPE, VALUE_APPLICATION_JSON)
            .and()
            .body(userFromRepo)
            .`when`()
            .post(URI_MANAGE)
            .then()
            .statusCode(200)
    }

    @Test
    fun ifUserExistsInDatabaseItWillBeDeletedViaDeleteEndpoint() {
        userCamelRepository.createEntity(testUser)
        val userFromRepo: User? = userRepository.findByUUID(UUID_TESTUSER_1)

        given()
            .header(HEADER_CONTENT_TYPE, VALUE_APPLICATION_JSON)
            .and()
            .body(userFromRepo)
            .`when`()
            .post(URI_DELETE)
            .then()
            .statusCode(200)
    }


}