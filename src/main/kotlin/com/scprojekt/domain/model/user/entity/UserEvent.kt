package com.scprojekt.domain.model.user.entity

import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.mimetidae.domain.shared.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import lombok.Builder
import java.util.*

@Entity
@Table(name = "userevent")
@Builder
open class UserEvent: BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userevent_seq")
    @SequenceGenerator(name = "userevent_seq", sequenceName = "USEREVENT_SEQ", allocationSize = 1, initialValue = 1)
    @Column(name = "usereventid")
    open var userEventId: Long? = null

    @Lob
    @Column(name="usereventuuid", unique = true, updatable = false, length = 36)
    @NotNull
    open var uuid: UUID? = null

    @Enumerated
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
}