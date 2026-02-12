package com.mr.websocket_chat.domain.mapper

import com.mr.websocket_chat.domain.jpa.ChatMessageEntity
import com.mr.websocket_chat.domain.rest.ChatMessageDTO

object ChatMessageMapper {

    fun toDTO(chatMessageEntity: ChatMessageEntity): ChatMessageDTO {
        return ChatMessageDTO(
            data = chatMessageEntity.data,
            room = ChatRoomMapper.toDTO(chatMessageEntity.room),
            sender = UserMapper.toDTO(chatMessageEntity.sender),
            timestamp = chatMessageEntity.timestamp,
            id = chatMessageEntity.id!!
        )
    }

}