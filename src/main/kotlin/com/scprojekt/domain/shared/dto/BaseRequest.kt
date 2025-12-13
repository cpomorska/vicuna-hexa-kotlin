package com.scprojekt.domain.shared.dto

import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor


@NoArgsConstructor
@AllArgsConstructor
open class BaseRequest {

    @NotNull(message = "Please provide an event type")
    var requestType: RequestType? = null
}