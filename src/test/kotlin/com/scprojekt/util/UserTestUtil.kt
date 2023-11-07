package com.scprojekt.util

import com.scprojekt.domain.model.user.dto.request.CreateUserRequest
import com.scprojekt.domain.model.user.dto.request.DeleteUserRequest
import com.scprojekt.domain.model.user.dto.request.UpdateUserRequest
import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.domain.model.user.entity.UserEvent
import com.scprojekt.domain.model.user.entity.UserNumber
import com.scprojekt.domain.model.user.entity.UserType
import com.scprojekt.domain.model.user.event.UserHandlingEvent
import com.scprojekt.infrastructure.mapping.VicunaObjectMapper
import java.util.*

const val TESTROLE = "testrole"
const val TESTUSER = "Testuser"
const val NEUUSER = "Testuser"
const val USER_ID_TESTUSER_1 = 1L
const val UUID_TESTUSER_1 = "586c2084-d545-4fac-b7d3-2319382df14f"
const val UUID_TESTUSER_2 = "35fa10da-594a-4601-a7b7-0a707a3c1ce7"

const val HEADER_CONTENT_TYPE = "Content-type"
const val VALUE_APPLICATION_JSON = "application/json"

const val URI_DELETE = "/api/store/user/delete"
const val URI_MANAGE = "/api/store/user/manage"
const val URI_CREATE = "/api/store/user/create"

internal class TestUtil {
    class TestUtil {
        companion object {
            @JvmStatic
            fun createTestUser(): User {
                val user = User()
                val userType = UserType()
                val userTypeList: MutableList<UserType> = ArrayList()
                val userNumber = UserNumber(UUID.randomUUID())
                userNumber.uuid = UUID.fromString(UUID_TESTUSER_1)
                userType.userTypeId = USER_ID_TESTUSER_1
                userType.userRoleType = TESTROLE
                userType.userTypeDescription = TESTUSER
                userTypeList.add(userType)
                user.userId = USER_ID_TESTUSER_1
                user.userName = TESTUSER
                user.userDescription = TESTUSER
                user.userNumber = userNumber
                user.userType = userType
                return user
            }

            @JvmStatic
            fun createTestUserEventStore(userHandlingEvent: UserHandlingEvent): UserEvent {
                val userEvent = UserEvent()
                userEvent.uuid = userHandlingEvent.eventid
                userEvent.userHandlingEvent = VicunaObjectMapper.getInstance().writeValueAsString(userHandlingEvent)
                return userEvent
            }

            @JvmStatic
            fun createUserRequest(user: User): CreateUserRequest {
                var createUserRequest = CreateUserRequest()
                createUserRequest.userName = user.userName
                createUserRequest.userDescription = user.userDescription
                createUserRequest.userType = user.userType
                createUserRequest.userNumber = user.userNumber
                return createUserRequest
            }

            @JvmStatic
            fun updateUserRequest(user: User): UpdateUserRequest {
                var updateUserRequest = UpdateUserRequest()
                updateUserRequest.userName = user.userName
                updateUserRequest.userUpdate = user.userNumber.uuid!!
                return updateUserRequest
            }

            @JvmStatic
            fun deleteUserRequest(user: User): DeleteUserRequest {
                var deleteUserRequest = DeleteUserRequest()
                deleteUserRequest.userName = NEUUSER
                deleteUserRequest.userNumber = user.userNumber
                return deleteUserRequest
            }
        }
    }
}