package com.mr.websocket_chat.domain.rest

import java.sql.Timestamp

data class ChatMessageDTO(
    val data: String,
    val room: ChatRoomDTO,
    val sender: UserDTO,
    val timestamp: Timestamp,
    val id: Long
)
