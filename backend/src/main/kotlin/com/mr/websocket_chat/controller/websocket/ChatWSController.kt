package com.mr.websocket_chat.controller.websocket

import com.mr.websocket_chat.domain.ChatMessageEntity
import com.mr.websocket_chat.service.ChatMessageService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller


@Controller
class ChatWSController @Autowired constructor(
	private val chatMessageService: ChatMessageService
){

	@Autowired
	private lateinit var messagingTemplate: SimpMessagingTemplate

	companion object {
		private val LOG = KotlinLogging.logger{}
	}

	@SubscribeMapping("/chat.listen.{roomId}")
	fun subscribeTopic(@DestinationVariable roomId: String): List<ChatMessageEntity> {
		LOG.debug { "SUBSCRIBE MESSAGE: $roomId" }
		return chatMessageService.loadNewestMessagesForRoom(roomId)
	}

	@MessageMapping("/chat.sendMessage")
	fun sendMessage(@Payload message: ChatMessageEntity) {
		LOG.debug { "RECEIVED MESSAGE: " + message.data + " FROM: " + message.sender}
		chatMessageService.saveMessage(message)
		messagingTemplate.convertAndSend("/topic/chat.listen." + message.room.id, message)
	}

}