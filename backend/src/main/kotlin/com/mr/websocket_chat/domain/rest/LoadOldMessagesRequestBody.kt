package com.mr.websocket_chat.domain.rest

data class LoadOldMessagesRequestBody(
    val numberOfMessagesToLoad: Int = 50,
    val olderThanMessageId: Long,
    val roomId: Long
)
