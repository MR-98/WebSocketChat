package com.mr.websocket_chat.domain.rest

data class JWTValidateResponseDTO(
    val valid: Boolean,
    val username: String?
)
