package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.exception.ChatRoomNotFoundException
import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.jpa.ChatRoomEntity
import com.mr.websocket_chat.domain.mapper.ChatRoomMapper
import com.mr.websocket_chat.domain.rest.ChatRoomDTO
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class ChatRoomService @Autowired constructor(
	private val chatRoomRepository: ChatRoomRepository,
	private val userRepository: UserRepository,
	private val chatRoomMapper: ChatRoomMapper
){

	@Transactional
	fun createChatRoom(roomName: String, creator: String): ChatRoomDTO {
		val creatorEntity = userRepository.findByUsername(creator) ?: throw UserNotFoundException()
		val createdRoom = chatRoomRepository.save(
			ChatRoomEntity(
				roomName,
				mutableSetOf(creatorEntity)
			)
		)
		return chatRoomMapper.toDTO(createdRoom)
	}

	@Transactional(readOnly = true)
	fun getChatRoomsForUser(username: String): List<ChatRoomDTO> {
		return chatRoomRepository.findByUsers_Username(username).map {
			chatRoomMapper.toDTO(it)
		}
	}

	@Transactional
	fun removeUserFromRoom(roomId: Long, username: String) {
		val roomEntity = chatRoomRepository.findByIdOrNull(roomId) ?: throw ChatRoomNotFoundException()
		val wasAnyoneRemoved = roomEntity.users.removeIf { element -> element.username == username }
		if(roomEntity.users.isEmpty()) {
			chatRoomRepository.deleteById(roomId)
		} else if(wasAnyoneRemoved) {
			chatRoomRepository.save(roomEntity)
		}
	}

	@Transactional(readOnly = true)
	fun chatRoomExists(roomId: Long): Boolean {
		return chatRoomRepository.existsById(roomId)
	}

	@Transactional
	fun deleteChatRoom(roomId: Long, deletingUser: String) {
		val roomEntity = chatRoomRepository.findByIdOrNull(roomId) ?: throw ChatRoomNotFoundException()
		if(isUserChatRoomMember(deletingUser, roomEntity)) {
			chatRoomRepository.deleteById(roomId)
		} else {
			throw RuntimeException("User cannot delete room to which he does not belong. User id: ${deletingUser}, room id: $roomId.")
		}
	}

	@Transactional(readOnly = true)
	fun isUserChatRoomMember(username: String, roomId: Long): Boolean {
		val room = chatRoomRepository.findById(roomId).getOrNull() ?: return false
		return isUserChatRoomMember(username, room)
	}

	fun isUserChatRoomMember(username: String, room: ChatRoomEntity): Boolean {
		return room.users.any { it.username == username }
	}

	@Transactional
	fun changeRoomName(roomId: Long, newRoomName: String, changingUser: String): ChatRoomDTO {
		val roomEntity = chatRoomRepository.findByIdOrNull(roomId) ?: throw ChatRoomNotFoundException()
		if(!isUserChatRoomMember(changingUser, roomEntity)) {
			throw RuntimeException("User cannot change the name of a room to which he does not belong. User id: ${changingUser}, room id: $roomId.")
		}

		roomEntity.name = newRoomName
		chatRoomRepository.save(roomEntity)
		return chatRoomMapper.toDTO(roomEntity)
	}
}