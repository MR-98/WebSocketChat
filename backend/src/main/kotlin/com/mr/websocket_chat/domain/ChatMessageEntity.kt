package com.mr.websocket_chat.domain

import com.mr.websocket_chat.service.CryptoConverter
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.sql.Timestamp

@Entity
@Table(name = "messages")
class ChatMessageEntity(
	@Convert(converter = CryptoConverter::class)
	val data: String,
	@ManyToOne
	@JoinColumn(name = "chat_room_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	val room: ChatRoomEntity,
	val sender: String,
	val timestamp: Timestamp,
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,
)
