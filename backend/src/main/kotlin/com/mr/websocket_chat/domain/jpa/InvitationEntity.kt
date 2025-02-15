package com.mr.websocket_chat.domain.jpa

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "invitations")
class InvitationEntity (
	@ManyToOne
	@JoinColumn(name = "chat_room_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	val room: ChatRoomEntity,
	@ManyToOne
	@JoinColumn(name = "invited_user")
	val invitedUser: UserEntity,
	@ManyToOne
	@JoinColumn(name = "inviting_user")
	val invitingUser: UserEntity,
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,
)