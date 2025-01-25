package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.ChatRoomEntity
import com.mr.websocket_chat.domain.InvitationEntity
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.repository.InvitationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.Limit

@Service
class InvitationService @Autowired constructor(
	private val invitationRepository: InvitationRepository,
	private val roomRepository: ChatRoomRepository
){
	fun saveInvitation(invitation: InvitationEntity) {
		invitationRepository.save(invitation)
	}

	fun loadInvitationsForUser(username: String): List<InvitationEntity> {
		return invitationRepository.getAllByInvitedUser_Username(username, Limit.of(50))
	}

	fun findInvitationForRoomAndUser(roomId: Long, username: String): InvitationEntity? {
		return invitationRepository.findByRoom_IdAndInvitedUser_Username(roomId, username)
	}

	fun acceptInvitation(invitation: InvitationEntity): ChatRoomEntity {
		invitation.room.users.add(invitation.invitedUser)
		val room = this.roomRepository.save(invitation.room)
		deleteInvitation(invitation)
		return room
	}

	fun deleteInvitation(invitation: InvitationEntity) {
		this.invitationRepository.deleteById(invitation.id)
	}

}