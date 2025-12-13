package com.scprojekt.domain.shared.dto

import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor


@NoArgsConstructor
@AllArgsConstructor
class BaseResponse {

    @NotNull(message = "Please provide a response type")
    var responseType: ResponseType? = null
}
