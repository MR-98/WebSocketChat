package com.mr.websocket_chat.domain.rest

data class InvitationToSaveDTO (
    val roomId: Long,
    val invitedUser: String,
)