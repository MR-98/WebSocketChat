package com.mr.websocket_chat.controller

import com.mr.websocket_chat.domain.ChatRoomEntity
import com.mr.websocket_chat.service.AuthUtils
import com.mr.websocket_chat.service.ChatRoomService
import com.mr.websocket_chat.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rooms")
class RoomController @Autowired constructor(
	private val chatRoomService: ChatRoomService,
	private val userService: UserService,
	private val authUtils: AuthUtils
) {

	@PostMapping
	fun createRoom(@RequestBody roomName: String): ResponseEntity<ChatRoomEntity> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername() ?: return ResponseEntity.badRequest().build()
		val userEntity = userService.findByUsername(currentlyAuthenticatedUsername) ?: return ResponseEntity.notFound().build()
		val newRoom = ChatRoomEntity(
			name = roomName,
			users = mutableSetOf(userEntity),
			id = 0
		)
		val savedRoom = chatRoomService.addChatRoom(newRoom)
		return ResponseEntity.ok(savedRoom)
	}

	@PutMapping
	fun renameRoom(@RequestBody room: ChatRoomEntity): ResponseEntity<ChatRoomEntity> {
		val roomEntity = chatRoomService.findById(room.id) ?: return ResponseEntity.badRequest().build()

		roomEntity.name = room.name
		chatRoomService.addChatRoom(roomEntity)
		return ResponseEntity.ok(roomEntity)
	}

	@DeleteMapping
	fun deleteRoom(@RequestBody room: ChatRoomEntity): ResponseEntity<String> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername() ?: return ResponseEntity.badRequest().build()
		val userEntity = userService.findByUsername(currentlyAuthenticatedUsername) ?: return ResponseEntity.notFound().build()
		if(!chatRoomService.isUserChatRoomMember(userEntity.username, room)) {
			return ResponseEntity.badRequest().build()
		}
		chatRoomService.deleteChatRoom(room.id)
		return ResponseEntity.ok().build()
	}

	@GetMapping
	fun getRooms(): List<ChatRoomEntity> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
		if(currentlyAuthenticatedUsername.isNullOrEmpty()) {
			return listOf()
		}
		return chatRoomService.getChatRoomsForUser(currentlyAuthenticatedUsername)
	}
}