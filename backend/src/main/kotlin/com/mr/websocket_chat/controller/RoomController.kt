package com.mr.websocket_chat.controller

import com.mr.websocket_chat.domain.ChatRoomEntity
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.repository.UserRepository
import com.mr.websocket_chat.service.AuthUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rooms")
class RoomController @Autowired constructor(
	private val roomRepository: ChatRoomRepository,
	private val userRepository: UserRepository,
	private val authUtils: AuthUtils
) {

	@PostMapping
	fun createRoom(@RequestBody room: ChatRoomEntity): ResponseEntity<ChatRoomEntity> {
		if (room.users.isEmpty()) {
			return ResponseEntity.badRequest().build()
		}
		val savedRoom = roomRepository.save(room)
		return ResponseEntity.ok(savedRoom)
	}

	@PostMapping("/{roomName}/addUser")
	fun addUserToRoom(@PathVariable roomName: String, @RequestParam username: String): ResponseEntity<String> {
		val room = roomRepository.findByName(roomName)
			?: return ResponseEntity.badRequest().body("Room not found")

		val user = userRepository.findByUsername(username)
			?: return ResponseEntity.badRequest().body("User not found")

		if (!room.users.contains(user)) {
			room.users.add(user)
			roomRepository.save(room)
			return ResponseEntity.ok("User added to the room")
		} else {
			return ResponseEntity.badRequest().body("User already in the room")
		}
	}

	@GetMapping
	fun getRooms(): List<ChatRoomEntity> {
		val currentlyAuthenticatedUsername = authUtils.getCurrentlyAuthenticatedUsername()
		if(currentlyAuthenticatedUsername.isNullOrEmpty()) {
			return listOf()
		}
		return roomRepository.findByUsers_Username(currentlyAuthenticatedUsername)
	}
}