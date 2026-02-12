package com.mr.websocket_chat.domain.mapper

import com.mr.websocket_chat.domain.jpa.UserEntity
import com.mr.websocket_chat.domain.rest.UserDTO

object UserMapper {

    fun toDTO(user: UserEntity): UserDTO {
        return UserDTO(
            username = user.username,
            firstName = user.firstName,
            lastName = user.lastName
        )
    }
}