package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.exception.ChatRoomNotFoundException
import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.jpa.ChatRoomEntity
import com.mr.websocket_chat.domain.rest.ChatRoomDTO
import com.mr.websocket_chat.domain.rest.UserDTO
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ChatRoomService @Autowired constructor(
	private val chatRoomRepository: ChatRoomRepository,
	private val userRepository: UserRepository,
){

	fun createChatRoom(roomName: String, creator: String): ChatRoomDTO {
		val creatorEntity = userRepository.findByUsername(creator) ?: throw UserNotFoundException()
		val createdRoom = chatRoomRepository.save(
			ChatRoomEntity(
				roomName,
				mutableSetOf(creatorEntity)
			)
		)
		println("Created Room: $createdRoom")
		return ChatRoomDTO(
            name = createdRoom.name,
            users = createdRoom.users.map {
				UserDTO(
					username = it.username,
					firstName = it.firstName,
					lastName = it.lastName
				)
			}.toMutableSet(),
            id = createdRoom.id!!,
        )
	}

	fun getChatRoomsForUser(username: String): List<ChatRoomDTO> {
		return chatRoomRepository.findByUsers_Username(username).map {
			ChatRoomDTO(
				name = it.name,
				users = it.users.map { user ->
					UserDTO(
						username = user.username,
						firstName = user.firstName,
						lastName = user.lastName
					)
				}.toMutableSet(),
				id = it.id!!,
			)
		}
	}

	fun removeUserFromRoom(roomId: Long, username: String) {
		val roomEntity = chatRoomRepository.findByIdOrNull(roomId) ?: throw ChatRoomNotFoundException()
		roomEntity.users.removeIf { element -> element.username == username }
		if(roomEntity.users.isEmpty()) {
			chatRoomRepository.deleteById(roomId)
		} else {
			chatRoomRepository.save(roomEntity)
		}
	}

	fun chatRoomExists(roomId: Long): Boolean {
		return chatRoomRepository.existsById(roomId)
	}

	fun deleteChatRoom(roomId: Long, deletingUser: String) {
		val roomEntity = chatRoomRepository.findByIdOrNull(roomId) ?: throw ChatRoomNotFoundException()
		val userEntity = userRepository.findByUsername(deletingUser) ?: throw UserNotFoundException()
		if(roomEntity.users.contains(userEntity)) {
			chatRoomRepository.deleteById(roomId)
		}
	}

	fun isUserChatRoomMember(username: String, roomId: Long): Boolean {
		val room = chatRoomRepository.findById(roomId).getOrNull() ?: return false
		return room.users.any { it.username == username }
	}

	fun changeRoomName(roomId: Long, newRoomName: String, changingUser: String): ChatRoomDTO {
		val roomEntity = chatRoomRepository.findByIdOrNull(roomId) ?: throw ChatRoomNotFoundException()
		val userEntity = userRepository.findByUsername(newRoomName) ?: throw UserNotFoundException()
		if(roomEntity.users.contains(userEntity)) {
			throw RuntimeException("User cannot change the name of a room to which he does not belong. User id: ${userEntity.username}, room id: $roomId.")
		}

		roomEntity.name = newRoomName
		chatRoomRepository.save(roomEntity)
		return ChatRoomDTO(
			name = roomEntity.name,
			users = roomEntity.users.map {
				UserDTO(
					username = it.username,
					firstName = it.firstName,
					lastName = it.lastName
				)
			}.toMutableSet(),
			id = roomEntity.id!!,
		)
	}
}