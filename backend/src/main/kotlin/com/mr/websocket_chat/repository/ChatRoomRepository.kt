package com.mr.websocket_chat.repository

import com.mr.websocket_chat.domain.ChatRoomEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository : JpaRepository<ChatRoomEntity, Long> {
	fun findByName(name: String): ChatRoomEntity?
	fun findByUsers_Username(username: String): List<ChatRoomEntity>
}