package com.mr.websocket_chat.interceptor.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mr.websocket_chat.domain.ChatMessageEntity
import com.mr.websocket_chat.service.AuthUtils
import com.mr.websocket_chat.service.ChatRoomService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component

@Component
class MessageHandler @Autowired constructor(
	private val authUtils: AuthUtils,
	private val chatRoomService: ChatRoomService
): WebSocketMessageHandler {

	companion object {
		private val LOG = KotlinLogging.logger{}
	}

	override fun handleMessage(message: Message<*>): Message<*>? {
		val headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java) ?: return null
		val principal = authUtils.getPrincipalFromAuthorizationHeader(headerAccessor) ?: return null
		val subscriberUsername = authUtils.getUsernameFromPrincipal(principal)
		val destination = headerAccessor.destination
		if(subscriberUsername.isNullOrEmpty() || (message.payload as ByteArray).isEmpty() || destination.isNullOrEmpty()) {
			return null
		}
		when(destination.substringBeforeLast(".")) {
			"/topic/chat.listen" -> {
				val chatMessage = try {
					jacksonObjectMapper().readValue(message.payload as ByteArray, ChatMessageEntity::class.java)
				} catch (e: Exception) {
					return null
				}
				if (chatMessage.sender != subscriberUsername ||
					!chatRoomService.isUserChatRoomMember(subscriberUsername, chatMessage.room.id)) {
					return null
				}
			}
			else -> {
				LOG.error { "Unknown destination: $destination" }
				return null
			}
		}

		return message
	}
}