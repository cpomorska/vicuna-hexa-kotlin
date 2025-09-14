package com.scprojekt.keycloak.integration

import dasniko.testcontainers.keycloak.KeycloakContainer
import jakarta.ws.rs.core.MediaType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import io.restassured.RestAssured.given
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo

private const val APPLICATION_OPENMETRICS_TEXT = "application/openmetrics-text"
private const val HEADER_CONTENT_TYPE = "Content-Type"
private const val REALM_DEVELOPMENT = "development"
private const val CLIENT_ID_BACKEND_SERVICE = "backend-service"
private const val TOKEN_DEVELOPMENT = "qQOkEGGd6JzzeDj0wkqjTFzrHdJiWdgz"
private const val USER_NAME_ALICE = "alice"
private const val USER_PW_ALICE = "alice"
private const val ENDPOINT_HEALTH = "/health"
private const val ENDPOINT_METRICS = "/metrics"

/**
 * Integration test class for interacting with a Keycloak instance to validate
 * health, metrics, and token retrieval functionalities.
 *
 * This test class is initialized with a `KeycloakIntegrationBase` setup, which
 * includes configuring and starting the Keycloak container before all tests and
 * stopping it afterward. It uses JUnit 5's test lifecycle annotations
 * for setup and execution of tests.
 *
 * It specifically verifies:
 * - Token generation when Keycloak is operational.
 * - Accessibility of the Keycloak health endpoint.
 * - Accessibility of the Keycloak metrics endpoint based on expected
 *   response status, format, and content.
 *
 * The `buildAdminClient` utility method aids in creating a Keycloak admin
 * client for interaction with the Keycloak server.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KeycloakIntegrationInstanceIT : KeycloakIntegrationBase() {

    private lateinit var keycloakAdminClient: Keycloak

    @BeforeEach
    fun init() {
        keycloakAdminClient = buildAdminClient(keycloakContainer!!, null, null)
    }

    @Test
    fun whenKeycloakIsHealthyARequestGetsAToken() {
        val token = keycloakAdminClient.tokenManager().accessToken.token
        assertThat(token).isNotNull()
    }

    @Test
    fun whenKeycloakIsHealthyTheHealthEndpointIsAccessible() {
        given()
            .header(HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .get(keycloakContainer!!.mgmtServerUrl + ENDPOINT_HEALTH)
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON)
            .body("status", equalTo("UP"))
    }

    @Test
    fun whenKeycloakIsHealthyTheMetricsEndpointIsAccessible() {
        // given
        given()
            .header(HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .get(keycloakContainer!!.mgmtServerUrl + ENDPOINT_METRICS)
            .then()
            .statusCode(200)
            .contentType(APPLICATION_OPENMETRICS_TEXT)
            .extract()
            .body().asString()
    }

    fun buildAdminClient(keycloak: KeycloakContainer, realm: String?, clientid: String?): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloak.authServerUrl)
            .realm(realm ?: REALM_DEVELOPMENT)
            .clientId(clientid ?: CLIENT_ID_BACKEND_SERVICE)
            .clientSecret(TOKEN_DEVELOPMENT)
            .username(USER_NAME_ALICE)
            .password(USER_PW_ALICE)
            .build()
    }
}