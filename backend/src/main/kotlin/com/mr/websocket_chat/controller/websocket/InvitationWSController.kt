package com.mr.websocket_chat.controller.websocket

import com.mr.websocket_chat.domain.exception.ChatRoomNotFoundException
import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.rest.InvitationDTO
import com.mr.websocket_chat.domain.rest.InvitationToSaveDTO
import com.mr.websocket_chat.service.InvitationService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller

@Controller
class InvitationWSController @Autowired constructor(
	private val invitationService: InvitationService
) {

	@Autowired
	private lateinit var messagingTemplate: SimpMessagingTemplate

	companion object {
		private val LOG = KotlinLogging.logger{}
	}

	@SubscribeMapping("/invitation.listen.{username}")
	fun subscribeTopic(@DestinationVariable username: String): List<InvitationDTO> {
		LOG.debug { "SUBSCRIBE INVITATION: $username" }
		return invitationService.loadInvitationsForUser(username)
	}

	@MessageMapping("/invitation.sendInvitation")
	fun sendInvitation(@Payload invitation: InvitationToSaveDTO, authentication: Authentication) {
		val currentlyAuthenticatedUsername = authentication.name
		LOG.debug { "RECEIVED INVITATION FOR: " + invitation.invitedUser + " TO JOIN ROOM: " + invitation.roomId}
		try {
			val savedInvitation = invitationService.saveInvitation(invitation, currentlyAuthenticatedUsername)
			messagingTemplate.convertAndSend("/topic/invitation.listen." + savedInvitation.invitedUser, savedInvitation)
		} catch (e: ChatRoomNotFoundException) {
			LOG.error("Error while sending invitation. Chatroom with id: ${invitation.roomId} not found.", e)
		} catch (e: UserNotFoundException) {
			LOG.error("Error while sending invitation. User with username: ${invitation.invitedUser} not found.", e)
		} catch (e: DataIntegrityViolationException) {
			LOG.info { "Invitation already exists. Skipping." }
		}

	}
}