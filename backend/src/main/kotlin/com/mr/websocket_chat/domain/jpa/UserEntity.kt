package com.mr.websocket_chat.domain.jpa

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity (
	@Id
	val username: String,
	val firstName: String,
	val lastName: String,
	@JsonIgnoreProperties("users")
	@ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
	val rooms: MutableList<ChatRoomEntity> = mutableListOf()
)