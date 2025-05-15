package com.scprojekt.domain.model.user.entity

import com.scprojekt.domain.shared.database.NoSQLInjection
import com.scprojekt.domain.shared.database.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import lombok.Builder

@Entity
@Table(name = "users")
@Builder
open class User : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ", allocationSize = 1, initialValue = 1)
    @Column(name = "userid")
    open var userId: Long? = null

    @ManyToOne(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "usertypeid")
    @NotNull
    open var userType: UserType = UserType()

    @NoSQLInjection
    @Column(name = "username", nullable = false)
    open lateinit var userName: String

    @OneToOne(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "usernumberid")
    @NotNull
    open lateinit var userNumber: UserNumber

    @NoSQLInjection
    @Column(name = "userdescription")
    open lateinit var userDescription: String

    @Version
    open var version = 0
}
