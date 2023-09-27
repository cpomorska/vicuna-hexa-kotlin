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
        val clazz: Class<*> = exchange.message.getBody(Any::class.java).javaClass
        exchange.message.setHeader("jpaUrl", clazz.name)
    }
}
