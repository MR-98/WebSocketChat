package com.mr.websocket_chat.domain.rest

data class RegisterRequestDTO(
    val username: String,
    val firstName: String,
    val lastName: String,
    val password: String
)
