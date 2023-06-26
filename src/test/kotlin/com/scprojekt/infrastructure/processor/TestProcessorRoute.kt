package com.scprojekt.infrastructure.processor

import io.quarkus.test.junit.TestProfile
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import org.apache.camel.builder.RouteBuilder

@ApplicationScoped
@TestProfile(JpaUrlProcessorTest::class)
class TestProcessorRoute : RouteBuilder() {
    @Inject
    @field:Default
    private lateinit var prepareJpaUrlProcessor: JpaUrlProcessor
    override fun configure() {
        from("direct:start")
            .process(prepareJpaUrlProcessor)
            .to("mock:resultErrormail")
    }
}