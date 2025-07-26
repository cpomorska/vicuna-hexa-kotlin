package com.scprojekt.infrastructure.routes

import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.repository.UserCamelRepository
import com.scprojekt.infrastructure.repository.UserJpaRepository
import com.scprojekt.util.*
import com.scprojekt.util.TestUtil.Companion.createTestUser
import io.quarkus.test.common.WithTestResource
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


@QuarkusTest
@WithTestResource(H2DatabaseTestResource::class)
@Transactional
class UserRestRouteTest {

    private lateinit var testUserEntity: UserEntity

    private val keycloakClient = KeycloakTestClient()

    private val USERNAME_ALICE_MANN = "alice"

    @Inject
    @field:Default
    lateinit var userCamelRepository: UserCamelRepository

    @Inject
    @field: Default
    lateinit var userRepository: UserJpaRepository

    @BeforeEach
    @Transactional
    fun setup() {
        testUserEntity = createTestUser()

        val userEntities: MutableList<UserEntity>? = userRepository.findAllToRemove()
        userEntities?.forEach(Consumer { u: UserEntity ->
            userRepository.removeEntity(u)
        })
    }

    @Test
    fun ifUserNotExistsInDatabaseItWillBeCreatedViaCreateEndpoint() {
        given()
            .auth().oauth2(getAccessToken(USERNAME_ALICE_MANN))
            .header(HEADER_CONTENT_TYPE, VALUE_APPLICATION_JSON)
            .and()
            .body(testUserEntity)
            .`when`()
            .post(URI_CREATE)
            .then()
            .statusCode(200)
            .body("uuid", equalTo(UUID_TESTUSER_1))
    }

    @Test
    fun ifUserExistsInDatabaseItWillBeUpdatedViaManageEndpoint() {
        userRepository.createEntity(testUserEntity)
        val userEntityFromRepo: UserEntity? = userRepository.findByUUID(UUID_TESTUSER_1)
        userEntityFromRepo!!.userName = "New User"

        given()
            .auth().oauth2(getAccessToken(USERNAME_ALICE_MANN))
            .header(HEADER_CONTENT_TYPE, VALUE_APPLICATION_JSON)
            .and()
            .body(testUserEntity)
            .`when`()
            .post(URI_MANAGE)
            .then()
            .statusCode(200)
            .assertThat()
            .body("uuid", equalTo(UUID_TESTUSER_1))
    }

    @Test
    fun ifUserExistsInDatabaseItWillBeDeletedViaDeleteEndpoint() {
        userCamelRepository.createEntity(testUserEntity)

        given()
            .auth().oauth2(getAccessToken(USERNAME_ALICE_MANN))
            .header(HEADER_CONTENT_TYPE, VALUE_APPLICATION_JSON)
            .and()
            .body(testUserEntity)
            .`when`()
            .post(URI_DELETE)
            .then()
            .statusCode(200)
            .body("uuid", equalTo(UUID_TESTUSER_1))
    }

    protected fun getAccessToken(userName: String?): String {
        return keycloakClient.getRealmAccessToken("development","alice","alice","backend-service","qQOkEGGd6JzzeDj0wkqjTFzrHdJiWdgz")
    }
}