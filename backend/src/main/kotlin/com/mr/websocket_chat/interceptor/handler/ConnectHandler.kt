package com.mr.websocket_chat.interceptor.handler

import com.mr.websocket_chat.config.jwt.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class ConnectHandler @Autowired constructor(
	private val jwtService: JwtService
): WebSocketMessageHandler {

	override fun handleMessage(message: Message<*>): Message<*>? {
		val accessor = MessageHeaderAccessor.getAccessor(
			message,
			StompHeaderAccessor::class.java
		) ?: return null
		val authHeader = accessor.getFirstNativeHeader("Authorization")
		val token = jwtService.extractTokenFromHeader(authHeader) ?: return null
		jwtService.validateToken(token)
		setSecurityContext(token, accessor)
		return message
	}

	private fun setSecurityContext(token: String, accessor: StompHeaderAccessor) {
		val username = jwtService.getUsernameFromToken(token)
			?: throw IllegalArgumentException("Invalid or missing JWT token")
		val authentication = UsernamePasswordAuthenticationToken(
			username,
			null,
			emptyList()
		)
		SecurityContextHolder.getContext().authentication = authentication
		accessor.user = authentication
	}
}