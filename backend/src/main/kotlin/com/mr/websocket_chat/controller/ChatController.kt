package com.mr.websocket_chat.controller

import com.mr.websocket_chat.domain.ChatMessageEntity
import com.mr.websocket_chat.service.ChatMessageService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class ChatController @Autowired constructor(
	private val chatMessageService: ChatMessageService
){

	@Autowired
	private lateinit var messagingTemplate: SimpMessagingTemplate

	companion object {
		private val LOG = KotlinLogging.logger{}
	}

	@MessageMapping("/chat.sendMessage")
	fun sendMessage(@Payload message: ChatMessageEntity) {
		LOG.debug { "RECEIVED MESSAGE: " + message.data + " FROM: " + message.sender}
		chatMessageService.saveMessage(message)
		messagingTemplate.convertAndSend("/topic/" + message.room.id, message)
	}

}