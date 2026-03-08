package com.mr.websocket_chat.domain.rest

import com.mr.websocket_chat.domain.enum.AttachmentType

data class AttachmentDTO(
    val id: Long,
    val type: AttachmentType,
    val fileName: String,
    val url: String,
    val size: Long?,
    val contentType: String?
)
