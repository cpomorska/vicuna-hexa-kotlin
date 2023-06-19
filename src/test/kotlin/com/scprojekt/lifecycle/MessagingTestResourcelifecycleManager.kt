package com.scprojekt.lifecycle

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import io.smallrye.reactive.messaging.memory.InMemoryConnector


class MessagingTestResourcelifecycleManager : QuarkusTestResourceLifecycleManager {

    override fun start(): Map<String, String>? {
        val environmentProperties: MutableMap<String, String> = HashMap()
        val incomingProperties: Map<String, String> = InMemoryConnector.switchIncomingChannelsToInMemory("users-in")
        val outgoingProperties: Map<String, String> = InMemoryConnector.switchOutgoingChannelsToInMemory("users-out")
        environmentProperties.putAll(incomingProperties)
        environmentProperties.putAll(outgoingProperties)
        return environmentProperties
    }

    override fun stop() {
        InMemoryConnector.clear()
    }
}