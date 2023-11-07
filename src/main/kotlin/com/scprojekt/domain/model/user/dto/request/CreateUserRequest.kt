package com.scprojekt.domain.model.user.dto.request

import com.scprojekt.domain.model.user.entity.UserNumber
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.domain.model.user.event.UserEventType
import com.scprojekt.domain.shared.dto.BaseRequest
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import lombok.NoArgsConstructor


@NoArgsConstructor
class CreateUserRequest: BaseRequest() {

    var userEventType = UserEventType.CREATE

    @NotNull(message = "Please provide a user type")
    var userType: UserType = UserType()

    @NotBlank(message = "Please provide username")
    @Size(min = 5, max = 255, message = "Please provide a valid username")
    lateinit var userName: String

    @NotNull(message = "Please provide a user number")
    lateinit var userNumber: UserNumber

    @NotBlank(message = "Please provide a user description")
    @Size(min = 5, max = 255, message = "Please provide a valid userdescription")
    lateinit var userDescription: String
}