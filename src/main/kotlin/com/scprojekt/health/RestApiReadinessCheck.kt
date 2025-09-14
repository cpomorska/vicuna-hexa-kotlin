package com.scprojekt.health

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Liveness
import org.eclipse.microprofile.health.Readiness

@Liveness
@Readiness
@ApplicationScoped
class RestApiReadinessCheck : HealthCheck {
    override fun call(): HealthCheckResponse? {
        return HealthCheckResponse.up("REST API bereit")
    }
}