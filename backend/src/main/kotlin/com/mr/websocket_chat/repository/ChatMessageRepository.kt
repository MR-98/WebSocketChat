package com.mr.websocket_chat.repository

import com.mr.websocket_chat.domain.jpa.ChatMessageEntity
import org.springframework.data.domain.Limit
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessageEntity, Long> {
	fun getAllByRoom_IdOrderByTimestampDesc(chatRoomId: Long, limit: Limit): List<ChatMessageEntity>
}