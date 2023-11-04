package com.scprojekt.infrastructure.routes

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.infrastructure.repository.UserCamelRepository
import com.scprojekt.infrastructure.repository.UserJpaRepository
import com.scprojekt.util.*
import com.scprojekt.util.TestUtil.Companion.createTestUser
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.keycloak.client.KeycloakTestClient
import io.restassured.RestAssured.given
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer


private const val USERNAME_ALICE_MANN = "alice"

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
class UserRestRouteTest {

    private lateinit var testUser: User

    private val keycloakClient = KeycloakTestClient()

    @Inject
    @field:Default
    lateinit var userCamelRepository: UserCamelRepository

    @Inject
    @field: Default
    lateinit var userRepository: UserJpaRepository

    @BeforeEach
    @Transactional
    fun setup() {
        testUser = createTestUser()

        val users: MutableList<User>? = userRepository.findAllToRemove()
        users?.forEach(Consumer { u: User ->
            userRepository.removeEntity(u)
        })
    }

    @Test
    fun ifUserNotExistsInDatabaseItWillBeCreatedViaCreateEndpoint() {
        given()
            .auth().oauth2(getAccessToken(USERNAME_ALICE_MANN))
            .header(HEADER_CONTENT_TYPE, VALUE_APPLICATION_JSON)
            .and()
            .body(testUser)
            .`when`()
            .post(URI_CREATE)
            .then()
            .statusCode(200)
            .body("uuid", equalTo(UUID_TESTUSER_1))
    }

    @Test
    fun ifUserExistsInDatabaseItWillBeUpdatedViaManageEndpoint() {
        userRepository.createEntity(testUser)
        val userFromRepo: User? = userRepository.findByUUID(UUID_TESTUSER_1)
        userFromRepo!!.userName = "New User"

        given()
            .auth().oauth2(getAccessToken(USERNAME_ALICE_MANN))
            .header(HEADER_CONTENT_TYPE, VALUE_APPLICATION_JSON)
            .and()
            .body(userFromRepo)
            .`when`()
            .post(URI_MANAGE)
            .then()
            .statusCode(200)
            .assertThat()
            .body("uuid", equalTo(UUID_TESTUSER_1))
    }

    @Test
    fun ifUserExistsInDatabaseItWillBeDeletedViaDeleteEndpoint() {
        userCamelRepository.createEntity(testUser)
        val userFromRepo: User? = userRepository.findByUUID(UUID_TESTUSER_1)

        given()
            .auth().oauth2(getAccessToken(USERNAME_ALICE_MANN))
            .header(HEADER_CONTENT_TYPE, VALUE_APPLICATION_JSON)
            .and()
            .body(testUser)
            .`when`()
            .post(URI_DELETE)
            .then()
            .statusCode(200)
            .body("uuid", equalTo(UUID_TESTUSER_1))
    }

    protected fun getAccessToken(userName: String?): String {
        return keycloakClient.getAccessToken(userName)
    }
}