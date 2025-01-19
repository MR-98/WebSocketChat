package com.mr.websocket_chat.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*

@Entity
@Table(name = "chat_rooms")
class ChatRoomEntity (
	var name: String,
	@JsonIgnoreProperties("rooms")
	@ManyToMany(fetch = FetchType.EAGER)
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