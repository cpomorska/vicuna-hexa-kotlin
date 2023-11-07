package com.scprojekt.infrastructure.mapping

import com.scprojekt.domain.model.user.entity.User
import com.scprojekt.util.TestUtil.Companion.createTestUser
import com.scprojekt.util.TestUtil.Companion.createUserRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class VicunaObjectMapperTest {

    @Test
    fun IfGetInstanceIsCalledAnInstanceIsCreated(){
        val mapper = VicunaObjectMapper.getInstance()
        assertThat(mapper).isNotNull.isInstanceOf(VicunaObjectMapper::class.java)
    }

    @Test
    fun IfAnObjectIsMappedToStringItIsAString(){
        val mapper = VicunaObjectMapper.getInstance()
        val result = mapper.writeValueAsString(createUserRequest(createTestUser()))
        assertThat(result).isNotNull.isInstanceOf(String::class.java)
    }

    @Test
    fun IfAStringIsMappedToObjectItIsAnObject(){
        val mapper = VicunaObjectMapper.getInstance()
        val result = mapper.writeValueAsString(createUserRequest(createTestUser()))
        assertThat(result).isNotNull.isInstanceOf(String::class.java)

        val objectresult =  mapper.readValue(result, User::class.java)
        assertThat(objectresult).isNotNull.isInstanceOf(User::class.java)

    }
}