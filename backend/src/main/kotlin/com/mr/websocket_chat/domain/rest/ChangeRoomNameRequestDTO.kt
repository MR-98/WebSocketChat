package com.mr.websocket_chat.domain.rest

data class ChangeRoomNameRequestDTO(
    val roomId: Long,
    val newRoomName: String
)
