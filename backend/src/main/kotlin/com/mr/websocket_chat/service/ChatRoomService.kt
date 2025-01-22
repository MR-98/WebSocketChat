package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.ChatRoomEntity
import com.mr.websocket_chat.repository.ChatRoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ChatRoomService @Autowired constructor(
	private val chatRoomRepository: ChatRoomRepository
){

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