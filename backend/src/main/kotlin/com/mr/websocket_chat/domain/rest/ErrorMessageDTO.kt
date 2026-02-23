package com.mr.websocket_chat.domain.rest

import com.mr.websocket_chat.domain.enum.ErrorCode

data class ErrorMessageDTO(
    val errorCode: ErrorCode,
)
