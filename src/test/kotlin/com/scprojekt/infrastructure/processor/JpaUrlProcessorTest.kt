package com.scprojekt.infrastructure.processor

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.util.TestUtil.Companion.createTestUser
import io.quarkus.test.junit.QuarkusTest
import org.apache.camel.EndpointInject
import org.apache.camel.Produce
import org.apache.camel.ProducerTemplate
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport
import org.junit.jupiter.api.Test

@QuarkusTest
class JpaUrlProcessorTest : CamelQuarkusTestSupport() {

    @Produce("direct:start")
    lateinit var producerTemplate: ProducerTemplate

    @EndpointInject("mock:resultErrormail")
    lateinit var mockEndpoint : MockEndpoint

    @Test
    fun process() {
        val testUser = createTestUser()
        producerTemplate.sendBody("direct:start", testUser)

        mockEndpoint.expectedBodiesReceived(User::class.java)
        mockEndpoint.expectedMessageCount(1)
        mockEndpoint.assertIsSatisfied()
    }
}