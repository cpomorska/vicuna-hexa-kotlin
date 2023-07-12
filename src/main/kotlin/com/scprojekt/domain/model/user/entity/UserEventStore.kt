package com.scprojekt.domain.model.user.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.util.*

@Entity
@Table(name = "benutzerevent")
open class UserEventStore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "benutzerevent_seq", sequenceName = "BENUTZEREVENTID_SEQ", allocationSize = 1)
    @Column(name = "benutzereventid")
    var userEventStoreId: Long? = null

    @Lob
    @Column(name="benutzerventuuid", unique = true, updatable = false, length = 36)
    @NotEmpty(message = "benutzerventuuid mann!")
    var uuid: UUID? = null

    @Lob
    @Column(name="benutzerbehandlungevent")
    @NotNull
    open lateinit var userHandlingEvent: String
}