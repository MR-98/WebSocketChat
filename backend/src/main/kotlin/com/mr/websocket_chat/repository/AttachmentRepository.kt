package com.mr.websocket_chat.repository

import com.mr.websocket_chat.domain.jpa.AttachmentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AttachmentRepository : JpaRepository<AttachmentEntity, Long> {
}