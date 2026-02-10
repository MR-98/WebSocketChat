package com.mr.websocket_chat.controller.rest

import com.mr.websocket_chat.domain.exception.InvitationNotFoundException
import com.mr.websocket_chat.domain.rest.ChatRoomDTO
import com.mr.websocket_chat.domain.rest.UserDTO
import com.mr.websocket_chat.service.AuthUtils
import com.mr.websocket_chat.service.InvitationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/invitations")
class InvitationRestController @Autowired constructor(
	private val authUtils: AuthUtils,
	private val invitationService: InvitationService
){

	@PostMapping("/accept/{roomId}")
	fun acceptInvitation(@PathVariable roomId: Long): ResponseEntity<ChatRoomDTO> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
			?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

		try {
			val newRoom = invitationService.acceptInvitationAndReturnNewRoom(roomId, currentlyAuthenticatedUsername)
			return ResponseEntity.ok(
				ChatRoomDTO(
					newRoom.name,
					users = newRoom.users.map { user ->
						UserDTO(
							username = user.username,
							firstName = user.firstName,
							lastName = user.lastName
						)
					}.toMutableSet(),
					id = newRoom.id!!,
				),
			)
		} catch (e: InvitationNotFoundException) {
			return ResponseEntity.notFound().build()
		}
	}

	@DeleteMapping("/reject/{roomId}")
	fun rejectInvitation(@PathVariable roomId: Long): ResponseEntity<String> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
			?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

		try {
		    invitationService.rejectInvitation(roomId, currentlyAuthenticatedUsername)
			return ResponseEntity.ok().build()
		} catch (e: InvitationNotFoundException) {
			return ResponseEntity.notFound().build()
		}
	}
}