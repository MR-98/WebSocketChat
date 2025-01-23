package com.mr.websocket_chat.domain

import jakarta.persistence.*

@Entity
@Table(name = "invitations")
class InvitationEntity (
	@ManyToOne
	@JoinColumn(name = "chat_room_id")
	val room: ChatRoomEntity,
	@ManyToOne
	@JoinColumn(name = "invited_user")
	val invitedUser: UserEntity,
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,
)