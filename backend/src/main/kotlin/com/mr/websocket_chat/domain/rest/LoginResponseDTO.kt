package com.mr.websocket_chat.domain.rest

data class LoginResponseDTO(
    val token: String,
    val username: String
)
