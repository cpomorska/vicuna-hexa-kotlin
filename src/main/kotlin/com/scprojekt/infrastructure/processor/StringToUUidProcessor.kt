package com.scprojekt.infrastructure.processor

import jakarta.enterprise.context.ApplicationScoped
import org.apache.camel.Exchange
import org.apache.camel.Processor

@ApplicationScoped
class StringToUUidProcessor : Processor {
    override fun process(exchange: Exchange) {
        var uuidString = exchange.getIn().getHeader("uuid") as String?
        exchange.getIn().setBody(uuidString)
    }
}
