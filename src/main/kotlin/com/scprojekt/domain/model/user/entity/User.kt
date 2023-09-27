package com.scprojekt.domain.model.user.entity

import com.scprojekt.domain.shared.SQLInjectionSafe
import com.scprojekt.mimetidae.domain.shared.BaseEntity
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

    @SQLInjectionSafe
    @Column(name = "username", nullable = false)
    open lateinit var userName: String

    @OneToOne(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "usernumberid")
    @NotNull
    open lateinit var userNumber: UserNumber

    @SQLInjectionSafe
    @Column(name = "userdescription")
    open lateinit var userDescription: String
}
