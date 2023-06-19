package com.scprojekt.domain.model.user.entity

import com.scprojekt.domain.shared.SQLInjectionSafe
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty

@Entity
@Table(name = "benutzer")
@NamedQuery(name = "getUUID", query = "SELECT u from User u WHERE u.userNumber.uuid = :uuid")
open class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "benutzerid_seq", sequenceName = "BENUTZERID_SEQ", allocationSize = 1)
    @Column(name = "benutzerid")
    open var userId: Long? = null

    @NotEmpty
    open lateinit var userType: String

    @SQLInjectionSafe
    @Column(name = "benutzername", nullable = false)
    open lateinit var userName: String

    @Embedded
    @Column(nullable = false)
    open lateinit var userNumber: UserNumber

    //@NotNull
    //@SQLInjectionSafe
    @Column(name = "benutzerdescription")
    open lateinit var userDescription: String
}
