package com.scprojekt.mimetidae.domain.model.user.dto

import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.shared.BaseRequest
import jakarta.validation.constraints.NotNull
import lombok.NoArgsConstructor
import java.util.*

@NoArgsConstructor
class UpdateUserRequest: BaseRequest() {

    override var userEventType: UserEventType = UserEventType.UPDATE

    @NotNull(message = "Please provide a usernumber")
    lateinit var userUpdate: UUID

    var userName: String? = null
    var userEnabled: Boolean? = null
    var userDescription: String? = null

}