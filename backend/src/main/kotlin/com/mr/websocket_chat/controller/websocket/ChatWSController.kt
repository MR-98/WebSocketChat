package com.mr.websocket_chat.controller.websocket

import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.rest.ChatMessageDTO
import com.mr.websocket_chat.domain.rest.ChatMessageToSaveDTO
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
	fun subscribeTopic(@DestinationVariable roomId: Long): List<ChatMessageDTO> {
		LOG.debug { "SUBSCRIBE MESSAGE: $roomId" }
		return chatMessageService.loadNewestMessagesForRoom(roomId)
	}

	@MessageMapping("/chat.sendMessage")
	fun sendMessage(@Payload message: ChatMessageToSaveDTO) {
		LOG.debug { "RECEIVED MESSAGE: " + message.data + " FROM: " + message.senderUsername}
		try {
			val savedMessage = chatMessageService.saveMessage(message)
			messagingTemplate.convertAndSend("/topic/chat.listen." + savedMessage.room.id, savedMessage)
		} catch (e: UserNotFoundException) {
			// TODO: error propagation to frontend
		}

	}

}