package com.mr.websocket_chat.domain.rest

data class InvitationDTO(
    val room: ChatRoomDTO,
    val invitedUser: UserDTO,
    val invitingUser: UserDTO,
    val id: Long
)
