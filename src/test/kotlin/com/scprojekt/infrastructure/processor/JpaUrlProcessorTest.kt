package com.scprojekt.infrastructure.processor

import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.util.TestUtil.Companion.createTestUser
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.QuarkusTestProfile
import org.apache.camel.EndpointInject
import org.apache.camel.Produce
import org.apache.camel.ProducerTemplate
import org.apache.camel.component.mock.MockEndpoint
import org.junit.jupiter.api.Test

@QuarkusTest
class JpaUrlProcessorTest : QuarkusTestProfile{

    @Produce("direct:start")
    lateinit var producerTemplate: ProducerTemplate

    @EndpointInject("mock:resultErrormail")
    lateinit var mockEndpoint : MockEndpoint

    @Test
    fun process() {
        val testUser = createTestUser()
        producerTemplate.sendBody("direct:start", testUser)

        mockEndpoint.expectedBodiesReceived(UserEntity::class.java)
        mockEndpoint.expectedMessageCount(1)
        mockEndpoint.assertIsSatisfied()
    }
}