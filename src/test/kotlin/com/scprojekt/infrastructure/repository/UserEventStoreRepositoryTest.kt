package com.scprojekt.infrastructure.repository

import com.scprojekt.domain.model.user.entity.UserEventStore
import com.scprojekt.domain.model.user.event.HandlingEventType
import com.scprojekt.domain.model.user.event.UserHandlingEventFactory
import com.scprojekt.domain.model.user.repository.UserStoreRepository
import com.scprojekt.util.UserTestUtil.Companion.createTestUser
import com.scprojekt.util.UserTestUtil.Companion.createTestUserEventStore
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.function.Consumer

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
class UserEventStoreRepositoryTest {

    @Inject
    lateinit var userStoreRepository: UserStoreRepository

    @AfterEach
    @Transactional
    fun teardown() {
        val users: MutableList<UserEventStore>? = userStoreRepository.findAllInRepository()
        users?.forEach(Consumer { u: UserEventStore ->
            userStoreRepository.removeEntity(u)
        })
    }

    @Test
    fun findByUUID() {
        val userEvent = UserHandlingEventFactory.getInstance().createUserHandlingEvent(HandlingEventType.CREATEUSER, createTestUser())
        val userEventStore = createTestUserEventStore(userEvent)
        userStoreRepository.createEntity(userEventStore)
        val result: UserEventStore? = userStoreRepository.findByUUID(userEventStore.uuid.toString())

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