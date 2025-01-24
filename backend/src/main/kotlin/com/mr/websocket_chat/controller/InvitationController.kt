package com.mr.websocket_chat.controller

import com.mr.websocket_chat.domain.InvitationEntity
import com.mr.websocket_chat.service.InvitationService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller

@Controller
class InvitationController @Autowired constructor(
	private val invitationService: InvitationService
) {

	@Autowired
	private lateinit var messagingTemplate: SimpMessagingTemplate

	companion object {
		private val LOG = KotlinLogging.logger{}
	}

	@SubscribeMapping("/invitation.listen.{username}")
	fun subscribeTopic(@DestinationVariable username: String): List<InvitationEntity> {
		LOG.debug { "SUBSCRIBE INVITATION: $username" }
		return invitationService.loadInvitationsForUser(username)
	}

	@MessageMapping("/invitation.sendInvitation")
	fun sendInvitation(@Payload invitation: InvitationEntity) {
		LOG.debug { "RECEIVED INVITATION FOR: " + invitation.invitedUser + " TO JOIN ROOM: " + invitation.room.id}
		invitationService.saveInvitation(invitation)
		messagingTemplate.convertAndSend("/topic/invitation.listen." + invitation.invitedUser.username, invitation)
	}
}