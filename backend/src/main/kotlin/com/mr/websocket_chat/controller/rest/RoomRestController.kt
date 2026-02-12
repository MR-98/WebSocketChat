package com.mr.websocket_chat.controller.rest

import com.mr.websocket_chat.domain.exception.ChatRoomNotFoundException
import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.rest.ChangeRoomNameRequestDTO
import com.mr.websocket_chat.domain.rest.ChatRoomDTO
import com.mr.websocket_chat.service.ChatRoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rooms")
class RoomRestController @Autowired constructor(
	private val chatRoomService: ChatRoomService
) {

	@PostMapping
	fun createRoom(@RequestBody roomName: String, authentication: Authentication): ResponseEntity<ChatRoomDTO> {
		val currentlyAuthenticatedUsername = authentication.name

		try {
			val savedRoom = chatRoomService.createChatRoom(roomName, currentlyAuthenticatedUsername)
			return ResponseEntity.ok(savedRoom)
		} catch (e: UserNotFoundException) {
			return ResponseEntity.notFound().build()
		}
	}

	@PutMapping
	fun renameRoom(@RequestBody body: ChangeRoomNameRequestDTO, authentication: Authentication): ResponseEntity<ChatRoomDTO> {
		val currentlyAuthenticatedUsername = authentication.name

		try {
			val roomWithNewName = chatRoomService.changeRoomName(body.roomId, body.newRoomName, currentlyAuthenticatedUsername)
			return ResponseEntity.ok(roomWithNewName)
		} catch (e: ChatRoomNotFoundException) {
			return ResponseEntity.notFound().build()
		} catch (e: RuntimeException) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
		}
	}

	@PostMapping("/leave/{roomId}")
	fun leaveRoom(@PathVariable roomId: Long, authentication: Authentication): ResponseEntity<String> {
		val currentlyAuthenticatedUsername = authentication.name

		try {
			chatRoomService.removeUserFromRoom(roomId, currentlyAuthenticatedUsername)
			return ResponseEntity.ok().build()
		} catch (e: ChatRoomNotFoundException) {
			return ResponseEntity.notFound().build()
		} catch (e: RuntimeException) {
			return ResponseEntity.internalServerError().build()
		}
	}

	@DeleteMapping("/{roomId}")
	fun deleteRoom(@PathVariable roomId: Long, authentication: Authentication): ResponseEntity<String> {
		val currentlyAuthenticatedUsername = authentication.name

		try {
			chatRoomService.deleteChatRoom(roomId, currentlyAuthenticatedUsername)
			return ResponseEntity.ok().build()
		} catch (e: ChatRoomNotFoundException) {
			return ResponseEntity.notFound().build()
		} catch (e: RuntimeException) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
		}
	}

	@GetMapping
	fun getRooms(authentication: Authentication): ResponseEntity<List<ChatRoomDTO>> {
		val currentlyAuthenticatedUsername = authentication.name

		val roomsList = chatRoomService.getChatRoomsForUser(currentlyAuthenticatedUsername)
		return ResponseEntity.ok(roomsList)
	}
}