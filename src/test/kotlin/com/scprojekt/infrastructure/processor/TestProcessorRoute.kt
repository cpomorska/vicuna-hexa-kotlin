package com.scprojekt.infrastructure.processor

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.apache.camel.builder.RouteBuilder

@ApplicationScoped
class TestProcessorRoute : RouteBuilder() {

    @Inject
    private lateinit var prepareJpaUrlProcessor: JpaUrlProcessor

    override fun configure() {
        from("direct:start")
            .process(prepareJpaUrlProcessor)
            .to("mock:resultErrormail")
    }
}