package com.scprojekt.domain.model.user.entity
import com.scprojekt.domain.shared.database.BaseEntity
import com.scprojekt.domain.shared.database.NoSQLInjection
import jakarta.persistence.*

@Entity
@Table(name = "usertype")
open class UserType : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usertype_seq")
    @SequenceGenerator(name = "usertype_seq", sequenceName = "USERTYPE_SEQ", allocationSize = 1, initialValue = 1)
    @Column(name="usertypeid")
    open var userTypeId: Long? =null

    @NoSQLInjection
    @Column(name="usertyperole")
    open var userRoleType: String? = null

    @NoSQLInjection
    @Column(name="usertypedescription")
    open var userTypeDescription: String?=null

    @NoSQLInjection
    @Column(name="usertypeenabled")
    open var userTypeEnabled: Boolean = true

    override fun toString(): String {
        return "UserType{" +
                "type='" + UserType::class.java.name + '\'' +
                '}'
    } // Optional: Equals and hashCode if needed
}
