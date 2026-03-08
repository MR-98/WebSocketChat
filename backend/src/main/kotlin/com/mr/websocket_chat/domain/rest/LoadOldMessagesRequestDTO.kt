package com.mr.websocket_chat.domain.rest

data class LoadOldMessagesRequestDTO(
    val numberOfMessagesToLoad: Int = 50,
    val olderThanMessageId: Long,
    val roomId: Long
)
