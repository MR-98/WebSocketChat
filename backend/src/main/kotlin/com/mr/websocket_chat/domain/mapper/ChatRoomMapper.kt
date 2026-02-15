package com.mr.websocket_chat.domain.mapper

import com.mr.websocket_chat.domain.jpa.ChatRoomEntity
import com.mr.websocket_chat.domain.rest.ChatRoomDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ChatRoomMapper @Autowired constructor(
    private val userMapper: UserMapper,
){

    fun toDTO(chatRoom: ChatRoomEntity): ChatRoomDTO {
        return ChatRoomDTO(
            name = chatRoom.name,
            users = chatRoom.users.map {
                userMapper.toDTO(it)
            }.toMutableSet(),
            id = chatRoom.id!!
        )
    }
}