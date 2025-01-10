package com.mr.websocket_chat.repository

import com.mr.websocket_chat.domain.ChatMessageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessageEntity, Long> {
}