package com.scprojekt.domain.model.user.entity;

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Lob
import jakarta.validation.constraints.NotEmpty
import java.io.Serializable
import java.util.*

@Embeddable
open class UserNumber() :Serializable {
    constructor(uuid: UUID?) : this()

    @Lob
    @Column(name="benutzernummer", unique = true, updatable = false, length = 36)
    @NotEmpty(message = "benutzernummer cannot be empty.")
    var uuid: UUID? = null
}
