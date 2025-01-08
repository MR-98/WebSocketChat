package com.mr.websocket_chat.controller

import com.mr.websocket_chat.domain.ChatMessage
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.service.WebSocketUtils
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class ChatController @Autowired constructor(
	private val roomRepository: ChatRoomRepository,
	private val webSocketUtils: WebSocketUtils
){

	@Autowired
	private lateinit var messagingTemplate: SimpMessagingTemplate

	companion object {
		private val LOG = KotlinLogging.logger{}
	}

	@MessageMapping("/chat.sendMessage")
	fun sendMessage(@Payload message: ChatMessage, headerAccessor: SimpMessageHeaderAccessor) {
		LOG.debug { "RECEIVED MESSAGE: " + message.data + " FROM: " + message.sender}
		val userFromHeader = webSocketUtils.getUsernameFromHeaderAccessor(headerAccessor)

		if(message.sender != userFromHeader) {
			LOG.error { "Sender in payload is different than authenticated user" }
			return
		}

		val room = roomRepository.findByName(message.room)
		if(room == null) {
			LOG.error { "Room not found." }
			return
		}

		if (!room.users.any { it.username == userFromHeader }) {
			LOG.error { "Access Denied: You are not a member of this room." }
			return
		}

		messagingTemplate.convertAndSend("/topic/" + message.room, message)
	}

}