package com.scprojekt.mimetidae.domain.shared

import com.scprojekt.domain.model.user.event.UserEventType
import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@NoArgsConstructor
@AllArgsConstructor
open class BaseRequest {

    @NotNull(message = "Please provide a handlingeventtype")
    open lateinit var userEventType: UserEventType
}