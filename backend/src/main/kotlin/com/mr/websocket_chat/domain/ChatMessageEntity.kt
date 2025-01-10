package com.mr.websocket_chat.domain

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "messages")
class ChatMessageEntity(
	val data: String,
	@ManyToOne
	@JoinColumn(name = "chat_room_id")
	val room: ChatRoomEntity,
	val sender: String,
	val timestamp: Timestamp,
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,
)
