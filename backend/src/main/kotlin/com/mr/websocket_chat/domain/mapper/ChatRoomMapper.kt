package com.mr.websocket_chat.domain.mapper

import com.mr.websocket_chat.domain.jpa.ChatRoomEntity
import com.mr.websocket_chat.domain.rest.ChatRoomDTO

object ChatRoomMapper {

    fun toDTO(chatRoom: ChatRoomEntity): ChatRoomDTO {
        return ChatRoomDTO(
            name = chatRoom.name,
            users = chatRoom.users.map {
                UserMapper.toDTO(it)
            }.toMutableSet(),
            id = chatRoom.id!!
        )
    }
}