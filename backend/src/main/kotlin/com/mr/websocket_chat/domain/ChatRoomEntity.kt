package com.mr.websocket_chat.domain

import jakarta.persistence.*

@Entity
@Table(name = "chat_rooms")
class ChatRoomEntity (
	val name: String,
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "room_users",
		joinColumns = [JoinColumn(name = "room_id")],
		inverseJoinColumns = [JoinColumn(name = "user_id")]
	)
	val users: MutableSet<UserEntity> = mutableSetOf(),
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long,
)