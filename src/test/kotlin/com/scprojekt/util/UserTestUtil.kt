package com.scprojekt.util

import com.scprojekt.domain.model.user.dto.request.CreateUserRequest
import com.scprojekt.domain.model.user.dto.request.DeleteUserRequest
import com.scprojekt.domain.model.user.dto.request.UpdateUserRequest
import com.scprojekt.domain.model.user.event.UserHandlingEvent
import com.scprojekt.infrastructure.mapping.VicunaObjectMapper
import com.scprojekt.infrastructure.persistence.entity.UserEntity
import com.scprojekt.infrastructure.persistence.entity.UserEventEntity
import com.scprojekt.infrastructure.persistence.entity.UserNumberEntity
import com.scprojekt.infrastructure.persistence.entity.UserTypeEntity
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
    companion object {
        @JvmStatic
        fun createTestUser(): UserEntity {
            val userEntity = UserEntity()
            val userTypeEntity = UserTypeEntity()
            val userTypeEntityList: MutableList<UserTypeEntity> = ArrayList()
            val userNumberEntity = UserNumberEntity(UUID.randomUUID())
            userNumberEntity.uuid = UUID.fromString(UUID_TESTUSER_1)
            //userType.userTypeId = USER_ID_TESTUSER_1
            userTypeEntity.userRoleType = TESTROLE
            userTypeEntity.userTypeDescription = TESTUSER
            userTypeEntityList.add(userTypeEntity)
            //user.userId = USER_ID_TESTUSER_1
            userEntity.userName = TESTUSER
            userEntity.userDescription = TESTUSER
            userEntity.userNumber = userNumberEntity
            userEntity.userType = userTypeEntity
            return userEntity
        }

        @JvmStatic
        fun createTestUserEventStore(userHandlingEvent: UserHandlingEvent): UserEventEntity {
            val userEventEntity = UserEventEntity()
            userEventEntity.uuid = userHandlingEvent.eventid
            userEventEntity.userHandlingEvent = VicunaObjectMapper.getInstance().writeValueAsString(userHandlingEvent)
            return userEventEntity
        }

        @JvmStatic
        fun createUserRequest(userEntity: UserEntity): CreateUserRequest {
            var createUserRequest = CreateUserRequest()
            createUserRequest.userName = userEntity.userName
            createUserRequest.userDescription = userEntity.userDescription
            createUserRequest.userTypeEntity = userEntity.userType
            createUserRequest.userNumberEntity = userEntity.userNumber
            return createUserRequest
        }

        @JvmStatic
        fun updateUserRequest(userEntity: UserEntity): UpdateUserRequest {
            var updateUserRequest = UpdateUserRequest()
            updateUserRequest.userName = userEntity.userName
            updateUserRequest.userUpdate = userEntity.userNumber.uuid!!
            return updateUserRequest
        }

        @JvmStatic
        fun deleteUserRequest(userEntity: UserEntity): DeleteUserRequest {
            var deleteUserRequest = DeleteUserRequest()
            deleteUserRequest.userName = NEUUSER
            deleteUserRequest.userNumberEntity = userEntity.userNumber
            return deleteUserRequest
        }
    }
}