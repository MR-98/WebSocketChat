package com.mr.websocket_chat.domain.rest

import java.sql.Timestamp

data class ChatMessageToSaveDTO(
    val data: String,
    val roomId: Long,
    val senderUsername: String,
    val timestamp: Timestamp,
    val id: Long
)
