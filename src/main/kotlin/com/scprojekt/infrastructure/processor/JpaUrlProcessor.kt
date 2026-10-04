package com.scprojekt.infrastructure.processor

import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Named
import org.apache.camel.Exchange
import org.apache.camel.Processor


@ApplicationScoped
@RegisterForReflection
@Named
class JpaUrlProcessor : Processor {

    override fun process(exchange: Exchange) {
        exchange.setProperty("jpaUrl", "com.scprojekt.vicuna.jpa")
        val body = requireNotNull(exchange.message.getBody(Any::class.java)) {
            "Message body must not be null when determining the JPA entity class"
        }
        val clazz: Class<*> = body.javaClass
        exchange.message.setHeader("jpaUrl", clazz.name)
    }
}
