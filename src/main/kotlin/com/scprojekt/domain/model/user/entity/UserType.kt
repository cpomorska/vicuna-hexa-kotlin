package com.scprojekt.domain.model.user.entity
import com.scprojekt.domain.shared.SQLInjectionSafe
import com.scprojekt.mimetidae.domain.shared.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "benutzertyp")
open class UserType: BaseEntity() {

    @Id
    @Column(name="benutzertypid")
    open var userTypeId: Long? =null

    @SQLInjectionSafe
    @Column(name="benutzerrolle")
    open var userRoleType: String? = null

    @SQLInjectionSafe
    @Column(name="benutzertypbeschreibung")
    open var userTypeDescription: String?=null
}
