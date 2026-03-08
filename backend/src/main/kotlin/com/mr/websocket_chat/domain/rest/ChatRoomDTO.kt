package com.mr.websocket_chat.domain.rest

data class ChatRoomDTO(
    var name: String,
    val users: MutableSet<UserDTO> = mutableSetOf(),
    val id: Long
)
