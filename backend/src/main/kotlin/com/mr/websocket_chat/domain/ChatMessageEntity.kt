package com.mr.websocket_chat.domain

import jakarta.persistence.*

@Entity
@Table(name = "messages")
class ChatMessageEntity(
	val data: String,
	@ManyToOne
	@JoinColumn(name = "chat_room_id")
	val room: ChatRoomEntity,
	val sender: String,
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,
)
