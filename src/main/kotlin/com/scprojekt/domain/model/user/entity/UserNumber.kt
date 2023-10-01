package com.scprojekt.domain.model.user.entity;

import com.scprojekt.domain.shared.database.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.util.*

@Entity
@Table(name ="usernumber")
open class UserNumber() : BaseEntity() {
    constructor(randomUUID: UUID) : this() {
        uuid = randomUUID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usernumber_seq")
    @SequenceGenerator(name = "usernumber_seq", sequenceName = "USERNUMBER_SEQ", allocationSize = 1, initialValue = 1)
    @Column(name="usernumberid")
    open var userTypeId: Long? =null

    @Lob
    @Column(name="usernumber", unique = true, updatable = false, length = 36)
    @NotNull(message = "usernumber cannot be empty.")
    open var uuid: UUID? = null
}
