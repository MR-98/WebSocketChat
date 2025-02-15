package com.mr.websocket_chat.interceptor.handler

import com.mr.websocket_chat.service.AuthUtils
import com.mr.websocket_chat.service.ChatRoomService
import com.mr.websocket_chat.service.UserService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component
import java.lang.NumberFormatException

@Component
class SubscribeHandler @Autowired constructor(
	private val authUtils: AuthUtils,
	private val chatRoomService: ChatRoomService,
	private val userService: UserService
): WebSocketMessageHandler {

	companion object {
		private val LOG = KotlinLogging.logger{}
	}

	override fun handleMessage(message: Message<*>): Message<*>? {
		val headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java) ?: return null
		val principal = authUtils.getPrincipalFromAuthorizationHeader(headerAccessor) ?: return null
		val subscriberUsername = authUtils.getUsernameFromPrincipal(principal)
		val destination = headerAccessor.destination
		if(subscriberUsername.isNullOrEmpty() || destination.isNullOrEmpty()) {
			return null
		}
		when(destination.substringBeforeLast(".")) {
			"/topic/chat.listen" -> {
				val roomId = try {
					destination.substringAfterLast(".").toLong()
				} catch (e: NumberFormatException) {
					return null
				}

				if(!chatRoomService.isUserChatRoomMember(subscriberUsername, roomId)) {
					return null
				}
			}
			"/topic/invitation.listen" -> {
				val username = try {
					destination.substringAfterLast(".")
				} catch (e: NumberFormatException) {
					return null
				}

				val userEntity = userService.findByUsername(username) ?: return null
				if(userEntity.username != subscriberUsername) {
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