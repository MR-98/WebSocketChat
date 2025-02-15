package com.mr.websocket_chat.domain.jpa

import com.mr.websocket_chat.service.CryptoConverter
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.sql.Timestamp

@Entity
@Table(name = "messages")
class ChatMessageEntity(
	@Convert(converter = CryptoConverter::class)
	@Column(columnDefinition = "TEXT")
	val data: String,
	@ManyToOne
	@JoinColumn(name = "chat_room_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	val room: ChatRoomEntity,
	@ManyToOne
	@JoinColumn(name = "sender")
	val sender: UserEntity,
	val timestamp: Timestamp,
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,
)
