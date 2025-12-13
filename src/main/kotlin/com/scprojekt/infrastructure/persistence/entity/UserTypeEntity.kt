package com.scprojekt.infrastructure.persistence.entity

import com.scprojekt.domain.shared.database.BaseEntity
import jakarta.persistence.*

/**
 * JPA entity for UserType.
 * This class is used for persistence and is separate from the domain UserType class.
 */
@Entity
@Table(name = "usertype")
open class UserTypeEntity : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usertype_seq")
    @SequenceGenerator(name = "usertype_seq", sequenceName = "USERTYPE_SEQ", allocationSize = 1, initialValue = 1)
    @Column(name = "usertypeid")
    open var userTypeId: Long? = null

    @Column(name = "usertyperole")
    open var userRoleType: String? = null

    @Column(name = "usertypedescription")
    open var userTypeDescription: String? = null

    @Column(name = "usertypeenabled")
    open var userTypeEnabled: Boolean = true

    override fun toString(): String {
        return "UserTypeEntity{" +
                "type='" + UserTypeEntity::class.java.name + '\'' +
                '}'
    }
}