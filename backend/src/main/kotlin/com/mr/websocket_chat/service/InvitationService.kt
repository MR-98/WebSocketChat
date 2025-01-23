package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.InvitationEntity
import com.mr.websocket_chat.repository.InvitationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.Limit

@Service
class InvitationService @Autowired constructor(
	private val invitationRepository: InvitationRepository
){
	fun saveInvitation(invitation: InvitationEntity) {
		invitationRepository.save(invitation)
	}

	fun loadInvitationsForUser(username: String): List<InvitationEntity> {
		return invitationRepository.getAllByInvitedUser_Username(username, Limit.of(50))
	}

}