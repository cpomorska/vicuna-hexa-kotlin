package com.scprojekt.infrastructure.persistence.entity

import com.scprojekt.domain.shared.database.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.util.*

/**
 * JPA entity for User.
 * This class is used for persistence and is separate from the domain User class.
 */
@Entity
@Table(name = "users")
open class UserEntity : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_SEQ", allocationSize = 1, initialValue = 1)
    @Column(name = "userid")
    open var userId: Long? = null

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "usertypeid")
    @NotNull
    open var userType: UserTypeEntity = UserTypeEntity()

    @Column(name = "username", nullable = false)
    open lateinit var userName: String

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "usernumberid")
    @NotNull
    open lateinit var userNumber: UserNumberEntity

    @Column(name = "userdescription")
    open lateinit var userDescription: String

    @Version
    open var version = 0
    
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var contactInfo: MutableList<ContactInfoEntity> = mutableListOf()
}