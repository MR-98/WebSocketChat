package com.mr.websocket_chat.controller.rest

import com.mr.websocket_chat.domain.jpa.ChatRoomEntity
import com.mr.websocket_chat.service.AuthUtils
import com.mr.websocket_chat.service.InvitationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/invitations")
class InvitationRestController @Autowired constructor(
	private val authUtils: AuthUtils,
	private val invitationService: InvitationService
){

	@PostMapping("/accept/{roomId}")
	fun acceptInvitation(@PathVariable roomId: Long): ResponseEntity<ChatRoomEntity> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
			?: return ResponseEntity.badRequest().build()
		val invitation = invitationService.findInvitationForRoomAndUser(roomId, currentlyAuthenticatedUsername)
			?: return ResponseEntity.badRequest().build()

		val result = invitationService.acceptInvitation(invitation)
		return ResponseEntity.ok(result)
	}

	@DeleteMapping("/reject/{roomId}")
	fun rejectInvitation(@PathVariable roomId: Long): ResponseEntity<String> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
			?: return ResponseEntity.badRequest().build()
		val invitation = invitationService.findInvitationForRoomAndUser(roomId, currentlyAuthenticatedUsername)
			?: return ResponseEntity.badRequest().build()

		invitationService.deleteInvitation(invitation)
		return ResponseEntity.ok().build()
	}
}