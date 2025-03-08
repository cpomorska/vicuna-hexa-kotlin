package com.scprojekt.domain.it

import dasniko.testcontainers.keycloak.KeycloakContainer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll


/**
 * Base class for integration testing with a Keycloak container.
 *
 * This class provides predefined setup and teardown functionality for running
 * a Keycloak container during integration tests. It includes a utility method
 * to retrieve the appropriate mapped port from the configured Keycloak container.
 *
 * Key features:
 * - Configures a Keycloak container with custom settings such as default admin credentials,
 *   health and metrics endpoints, a specific context path, and initial realm imports.
 * - Manages lifecycle through static `setup` and `teardown` methods annotated with JUnit 5
 *   lifecycle annotations.
 * - Provides a method to fetch the mapped HTTP or HTTPS port when the Keycloak container
 *   is operational.
 */
open class KeycloakIntegrationBase {
    open fun getMappedPortFromKeycloak(https: Boolean): Int =
        keycloakContainer?.takeIf { it.isHealthy }?.run {
            if (https) httpsPort else httpPort
        } ?: 0

    companion object {
        var keycloakContainer: KeycloakContainer? = null

        @JvmStatic
        @BeforeAll
        fun setup() {
            keycloakContainer = KeycloakContainer("quay.io/keycloak/keycloak:latest")
                .apply {
                    withCreateContainerCmdModifier { cmd -> cmd.withName("keycloak-integration-test") }
                    withEnabledMetrics()
                    withAdminUsername("kadmin")
                    withAdminPassword("kadmin")
                    withContextPath("/auth")
                    withEnv("KC_HEALTH_ENABLED", "true")
                    withEnv("KC_METRICS_ENABLED", "true")
                    withProviderClassesFrom("target/classes/")
                    withRealmImportFiles("dev-realm.json")
                }
                .also { it.start() }
        }

        @JvmStatic
        @AfterAll
        fun teardown() {
            if(keycloakContainer != null && keycloakContainer?.isRunning == true)
            keycloakContainer?.stop()
        }
    }
}