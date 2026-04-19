package com.scprojekt.infrastructure.persistence.entity

import com.scprojekt.domain.shared.database.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

/**
 * JPA entity for ContactInfo.
 * This class is used for persistence and is separate from the domain ContactInfo value object.
 */
@Entity
@Table(name = "contact_info")
open class ContactInfoEntity : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_info_seq")
    @SequenceGenerator(name = "contact_info_seq", sequenceName = "CONTACT_INFO_SEQ", allocationSize = 1, initialValue = 1)
    @Column(name = "contact_info_id")
    open var contactInfoId: Long? = null

    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false)
    open lateinit var email: String

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    @Column(name = "phone")
    open var phone: String? = null

    @ManyToOne
    @JoinColumn(name = "userid")
    open var user: UserEntity? = null
}