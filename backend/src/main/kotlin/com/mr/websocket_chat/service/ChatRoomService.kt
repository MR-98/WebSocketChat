package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.jpa.ChatRoomEntity
import com.mr.websocket_chat.repository.ChatRoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ChatRoomService @Autowired constructor(
	private val chatRoomRepository: ChatRoomRepository
){

	fun addChatRoom(room: ChatRoomEntity): ChatRoomEntity {
		return chatRoomRepository.save(room)
	}

	fun findById(roomId: Long): ChatRoomEntity? {
		return chatRoomRepository.findByIdOrNull(roomId)
	}

	fun getChatRoomsForUser(username: String): List<ChatRoomEntity> {
		return chatRoomRepository.findByUsers_Username(username)
	}

	fun removeUserFromRoom(username: String, roomId: Long): Boolean {
		val roomEntity = chatRoomRepository.findByIdOrNull(roomId) ?: return false
		roomEntity.users.removeIf { element -> element.username == username }
		if(roomEntity.users.isEmpty()) {
			chatRoomRepository.deleteById(roomId)
		} else {
			chatRoomRepository.save(roomEntity)
		}
		return true
	}

	fun chatRoomExists(roomId: Long): Boolean {
		return chatRoomRepository.existsById(roomId)
	}

	fun deleteChatRoom(roomId: Long) {
		chatRoomRepository.deleteById(roomId)
	}

	fun isUserChatRoomMember(username: String, roomId: Long): Boolean {
		val room = chatRoomRepository.findById(roomId).getOrNull() ?: return false
		return isUserChatRoomMember(username, room)
	}

	fun isUserChatRoomMember(username: String, room: ChatRoomEntity): Boolean {
		return room.users.any { it.username == username }
	}
}