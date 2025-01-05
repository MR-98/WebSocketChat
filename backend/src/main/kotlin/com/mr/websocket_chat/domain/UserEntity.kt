package com.mr.websocket_chat.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity (
	val username: String,
	@JsonIgnoreProperties("users")
	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
	val rooms: MutableList<ChatRoomEntity> = mutableListOf(),
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,
)