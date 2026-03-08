package com.mr.websocket_chat.domain.jpa

interface AttachmentIdAndS3KeyProjection {
    fun getId(): Long
    fun getS3Key(): String
}