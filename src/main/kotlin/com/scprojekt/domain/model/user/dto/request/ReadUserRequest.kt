package com.scprojekt.domain.model.user.dto.request

import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.shared.dto.BaseRequest
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import lombok.NoArgsConstructor
import java.util.*

@NoArgsConstructor
class ReadUserRequest: BaseRequest() {

    var userEventType: UserEventType = UserEventType.READ

    var userType: UserType = UserType()

    @Size(min = 0, max = 255, message = "Please provide a valid username")
    lateinit var userName: String

    @NotNull(message = "Please provide a user number")
    lateinit var userNumber: UUID

    @Size(min = 0, max = 255, message = "Please provide a valid userdescription")
    lateinit var userDescription: String
}