package com.scprojekt.lifecycle

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import io.smallrye.reactive.messaging.memory.InMemoryConnector


class MessagingTestResourcelifecycleManager : QuarkusTestResourceLifecycleManager {

    override fun start(): Map<String, String>? {
        val environmentProperties = HashMap<String, String>()
        val incomingProperties = InMemoryConnector.switchIncomingChannelsToInMemory("users-in")
        val outgoingProperties = InMemoryConnector.switchOutgoingChannelsToInMemory("users-out")

        environmentProperties.run {
            putAll(incomingProperties)
            putAll(outgoingProperties)
        }

        return environmentProperties
    }

    override fun stop() {
        InMemoryConnector.clear()
    }
}