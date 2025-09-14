package com.scprojekt.health

import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.microprofile.health.HealthCheckResponse
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mockito.mock

@QuarkusTest
class RestApiReadinessCheckTest {

    @InjectMocks
    var mock: RestApiReadinessCheck = mock(RestApiReadinessCheck::class.java)

    @Test
    fun call_isMocked() {
        org.mockito.Mockito.`when`(mock.call()).thenReturn(HealthCheckResponse.up("mocked-ready"))
        val response = mock.call()
        assertThat(response).isNotNull
        assertThat(response!!.status).isEqualTo(HealthCheckResponse.Status.UP)
        assertThat(response.name).isEqualTo("mocked-ready")
    }

}