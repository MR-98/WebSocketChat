package com.mr.websocket_chat.interceptor.handler

import com.mr.websocket_chat.service.AuthUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component

@Component
class ConnectHandler @Autowired constructor(
	private val authUtils: AuthUtils,
): WebSocketMessageHandler {

	override fun handleMessage(message: Message<*>): Message<*>? {
		val headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java) ?: return null
		authUtils.getPrincipalFromAuthorizationHeader(headerAccessor) ?: return null
		return message
	}
}