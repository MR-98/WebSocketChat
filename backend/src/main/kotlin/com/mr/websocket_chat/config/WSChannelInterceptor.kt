package com.mr.websocket_chat.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mr.websocket_chat.domain.ChatMessage
import com.mr.websocket_chat.service.ChatRoomService
import com.mr.websocket_chat.service.JwtUtils
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component

@Component
class WSChannelInterceptor @Autowired constructor(
	private val jwtUtils: JwtUtils,
	private val chatRoomService: ChatRoomService
) : ChannelInterceptor {

	companion object {
		private val LOG = KotlinLogging.logger{}
	}
	// TODO: refactor
	override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
		val headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java) ?: return null
		when (headerAccessor.messageType) {
			SimpMessageType.CONNECT -> {
				jwtUtils.getPrincipalFromAuthorizationHeader(headerAccessor) ?: return null
			}
			SimpMessageType.SUBSCRIBE -> {
				val principal = jwtUtils.getPrincipalFromAuthorizationHeader(headerAccessor) ?: return null
				val subscriberUsername = jwtUtils.getUsernameFromPrincipal(principal)
				val topic = headerAccessor.destination
				if(subscriberUsername.isNullOrEmpty() || topic.isNullOrEmpty()) {
					return null
				}
				val roomName = topic.substringAfterLast("/")
				if(!chatRoomService.isUserChatRoomMember(subscriberUsername, roomName)) {
					return null
				}
			}
			SimpMessageType.MESSAGE -> {
				val principal = jwtUtils.getPrincipalFromAuthorizationHeader(headerAccessor) ?: return null
				val subscriberUsername = jwtUtils.getUsernameFromPrincipal(principal)
				if(subscriberUsername.isNullOrEmpty() || (message.payload as ByteArray).isEmpty()) {
					return null
				}
				try {
					val chatMessage = jacksonObjectMapper().readValue(message.payload as ByteArray, ChatMessage::class.java)
					if(chatMessage.sender != subscriberUsername || !chatRoomService.isUserChatRoomMember(subscriberUsername, chatMessage.room)) {
						return null
					}
				} catch (e: Exception) {
					return null
				}
			}
			else -> {}
		}

		return message
	}
}