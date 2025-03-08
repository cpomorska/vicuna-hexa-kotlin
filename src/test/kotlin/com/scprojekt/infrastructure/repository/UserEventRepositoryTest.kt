package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.entity.UserEvent
import com.scprojekt.domain.model.user.event.UserEventFactory
import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.model.user.repository.UserEventRepository
import com.scprojekt.util.TestUtil.Companion.createTestUser
import com.scprojekt.util.TestUtil.Companion.createTestUserEventStore
import io.quarkus.test.common.WithTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.function.Consumer

@Disabled
@QuarkusTest
@WithTestResource(H2DatabaseTestResource::class)
class UserEventRepositoryTest {

    @Inject
    lateinit var userEventRepository: UserEventRepository

    @AfterEach
    @Transactional
    fun teardown() {
        val users: MutableList<UserEvent>? = userEventRepository.findAllToRemove()
        users?.forEach(Consumer { u: UserEvent ->
            userEventRepository.removeEntity(u)
        })
    }

    @Test
    fun findByUUID() {
        val userEvent = UserEventFactory.getInstance().createUserEvent(UserEventType.CREATE, createTestUser())
        val userEventStore = createTestUserEventStore(userEvent)
        userEventRepository.createEntity(userEventStore)
        val result: UserEvent? = userEventRepository.findByUUID(userEventStore.uuid)

        Assertions.assertThat(result?.uuid).isEqualTo(userEventStore.uuid)
    }

    @Test
    fun findAllInRepository() {
    }

    @Test
    fun findByIdInRepository() {
    }

    @Test
    fun createEntity() {
    }

    @Test
    fun removeEntity() {
    }

    @Test
    fun updateEntity() {
    }
}