package com.mr.websocket_chat.controller

import com.mr.websocket_chat.domain.ChatMessage
import com.mr.websocket_chat.repository.ChatRoomRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class ChatController @Autowired constructor(
	private val roomRepository: ChatRoomRepository,
){

	@Autowired
	private lateinit var messagingTemplate: SimpMessagingTemplate

	companion object {
		private val LOG = KotlinLogging.logger{}
	}

	@MessageMapping("/chat.sendMessage")
	fun sendMessage(@Payload message: ChatMessage) {
		LOG.warn { "RECEIVED MESSAGE: " + message.data + " FROM: " + message.sender}

		val room = roomRepository.findByName(message.room) ?: throw RuntimeException("Room not found.")
		// TODO: replace message.sender with SecurityContextHolder.getContext().getAuthentication().getName()
		if (!room.users.any { it.username == message.sender }) {
			throw RuntimeException("Access Denied: You are not a member of this room.")
		}

		messagingTemplate.convertAndSend("/topic/" + message.room, message)
	}

}