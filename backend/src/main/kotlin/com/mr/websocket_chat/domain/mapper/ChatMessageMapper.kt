package com.mr.websocket_chat.domain.mapper

import com.mr.websocket_chat.domain.jpa.ChatMessageEntity
import com.mr.websocket_chat.domain.rest.ChatMessageDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ChatMessageMapper @Autowired constructor(
    private val chatRoomMapper: ChatRoomMapper,
    private val userMapper: UserMapper,
    private val attachmentMapper: AttachmentMapper
){

    fun toDTO(chatMessageEntity: ChatMessageEntity): ChatMessageDTO {
        return ChatMessageDTO(
            data = chatMessageEntity.data,
            room = chatRoomMapper.toDTO(chatMessageEntity.room),
            sender = userMapper.toDTO(chatMessageEntity.sender),
            timestamp = chatMessageEntity.timestamp,
            attachments = chatMessageEntity.attachments.map {
                attachmentMapper.toDTO(it)
            },
            id = chatMessageEntity.id!!
        )
    }

}