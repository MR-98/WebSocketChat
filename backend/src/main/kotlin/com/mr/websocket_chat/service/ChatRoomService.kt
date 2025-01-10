package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.ChatRoomEntity
import com.mr.websocket_chat.repository.ChatRoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChatRoomService @Autowired constructor(
	private val chatRoomRepository: ChatRoomRepository
){

	fun isUserChatRoomMember(username: String, roomName: String): Boolean {
		val room = chatRoomRepository.findByName(roomName) ?: return false
		return isUserChatRoomMember(username, room)
	}

	fun isUserChatRoomMember(username: String, room: ChatRoomEntity): Boolean {
		return room.users.any { it.username == username }
	}
}