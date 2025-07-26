package com.scprojekt.infrastructure.persistence.entity

import com.scprojekt.domain.shared.database.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.util.*

/**
 * JPA entity for UserNumber.
 * This class is used for persistence and is separate from the domain model.
 */
@Entity
@Table(name = "usernumber")
open class UserNumberEntity() : BaseEntity() {
    constructor(randomUUID: UUID?) : this() {
        uuid = randomUUID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usernumber_seq")
    @SequenceGenerator(name = "usernumber_seq", sequenceName = "USERNUMBER_SEQ", allocationSize = 1, initialValue = 1)
    @Column(name = "usernumberid")
    open var userNumberId: Long? = null

    @Lob
    @Column(name = "usernumber", unique = true, updatable = false, length = 36)
    @NotNull(message = "usernumber cannot be empty.")
    open var uuid: UUID? = null
}