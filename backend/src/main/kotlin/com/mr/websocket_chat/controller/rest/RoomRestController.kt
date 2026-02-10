package com.mr.websocket_chat.controller.rest

import com.mr.websocket_chat.domain.exception.ChatRoomNotFoundException
import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.rest.ChangeRoomNameRequestDTO
import com.mr.websocket_chat.domain.rest.ChatRoomDTO
import com.mr.websocket_chat.service.AuthUtils
import com.mr.websocket_chat.service.ChatRoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rooms")
class RoomRestController @Autowired constructor(
	private val chatRoomService: ChatRoomService,
	private val authUtils: AuthUtils
) {

	@PostMapping
	fun createRoom(@RequestBody roomName: String): ResponseEntity<ChatRoomDTO> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
			?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

		try {
			val savedRoom = chatRoomService.createChatRoom(roomName, currentlyAuthenticatedUsername)
			return ResponseEntity.ok(savedRoom)
		} catch (e: UserNotFoundException) {
			return ResponseEntity.notFound().build()
		}
	}

	@PutMapping
	fun renameRoom(@RequestBody room: ChangeRoomNameRequestDTO): ResponseEntity<ChatRoomDTO> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
			?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
		try {
			val roomWithNewName = chatRoomService.changeRoomName(room.roomId, room.newRoomName, currentlyAuthenticatedUsername)
			return ResponseEntity.ok(roomWithNewName)
		} catch (e: ChatRoomNotFoundException) {
			return ResponseEntity.notFound().build()
		} catch (e: UserNotFoundException) {
			return ResponseEntity.notFound().build()
		} catch (e: RuntimeException) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
		}
	}

	@PostMapping("/leave/{roomId}")
	fun leaveRoom(@PathVariable roomId: Long): ResponseEntity<String> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
			?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

		try {
			chatRoomService.removeUserFromRoom(roomId, currentlyAuthenticatedUsername)
			return ResponseEntity.ok().build()
		} catch (e: ChatRoomNotFoundException) {
			return ResponseEntity.notFound().build()
		} catch (e: RuntimeException) {
			return ResponseEntity.internalServerError().build()
		}
	}

	@DeleteMapping
	fun deleteRoom(@PathVariable roomId: Long): ResponseEntity<String> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
			?: return ResponseEntity.badRequest().build()

		try {
			chatRoomService.deleteChatRoom(roomId, currentlyAuthenticatedUsername)
		} catch (e: ChatRoomNotFoundException) {
			return ResponseEntity.notFound().build()
		} catch (e: UserNotFoundException) {
			return ResponseEntity.notFound().build()
		}

		return ResponseEntity.ok().build()
	}

	@GetMapping
	fun getRooms(): ResponseEntity<List<ChatRoomDTO>> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
			?: return ResponseEntity.badRequest().build()

		val roomsList = chatRoomService.getChatRoomsForUser(currentlyAuthenticatedUsername)
		return ResponseEntity.ok(roomsList)
	}
}