package com.scprojekt.infrastructure.processor

import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.util.TestUtil.Companion.createTestUser
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.apache.camel.CamelContext
import org.apache.camel.EndpointInject
import org.apache.camel.Produce
import org.apache.camel.ProducerTemplate
import org.apache.camel.component.mock.MockEndpoint
import org.junit.jupiter.api.Test

@QuarkusTest
class JpaUrlProcessorTest {

    @Inject
    private lateinit var camelContext: CamelContext

    @Produce("direct:start")
    lateinit var producerTemplate: ProducerTemplate

    @EndpointInject("mock:resultErrormail")
    lateinit var mockEndpoint: MockEndpoint

    @Test
    fun process() {
        val testUser: UserEntity = createTestUser()
        producerTemplate.sendBody("direct:start", testUser)

        mockEndpoint.expectedMessageCount(1)
        mockEndpoint.message(0).body().isInstanceOf(UserEntity::class.java)
        mockEndpoint.assertIsSatisfied()
    }
}
