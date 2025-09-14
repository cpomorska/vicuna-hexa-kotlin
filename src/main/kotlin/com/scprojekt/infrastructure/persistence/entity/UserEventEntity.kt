package com.scprojekt.infrastructure.persistence.entity

import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.shared.database.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.Version
import jakarta.validation.constraints.NotNull
import lombok.Builder
import java.util.UUID

@Entity
@Table(name = "userevent")
@Builder
open class UserEventEntity: BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userevent_seq")
    @SequenceGenerator(name = "userevent_seq", sequenceName = "USEREVENT_SEQ", allocationSize = 1, initialValue = 1)
    @Column(name = "usereventid")
    open var userEventId: Long? = null

    @Lob
    @Column(name="usereventuuid", unique = true, updatable = false, length = 36)
    @NotNull
    open var uuid: UUID? = null

    @Enumerated(EnumType.STRING)
    @Column(name="usereventtype")
    @NotNull
    open var userEventType: UserEventType? = null

    @Lob
    @Column(name="userhandlingevent")
    @NotNull
    open lateinit var userHandlingEvent: String

    @Column(name="isremovable")
    @NotNull
    open var isRemovable: Boolean = false

    @Version
    open var version = 0
}