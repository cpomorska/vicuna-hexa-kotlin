package com.scprojekt.domain.model.user.dto

import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import java.util.*

@NoArgsConstructor
@AllArgsConstructor
class UuidResponse() {
    constructor(uuid: UUID) : this() {
        this.uuid = uuid
    }

    @NotNull(message = "Please provide a usernumber")
    lateinit var uuid: UUID
}