package com.mr.websocket_chat.config

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.stereotype.Component

@Component
class WSChannelInterceptor @Autowired constructor(
	private val jwtDecoder: JwtDecoder,
	private val jwtAuthenticationConverter: JwtAuthenticationConverter
) : ChannelInterceptor {

	companion object {
		private val LOG = KotlinLogging.logger{}
	}

	override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
		val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java) ?: return null
		if (accessor.messageType in listOf(SimpMessageType.CONNECT, SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE)) {
			val authHeader = accessor.getNativeHeader("Authorization")?.firstOrNull()
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				val token = authHeader.substringAfter("Bearer ")
				try {
					val jwt = jwtDecoder.decode(token)
					val authentication = jwtAuthenticationConverter.convert(jwt)
					accessor.user = authentication
				} catch (e: JwtException) {
					LOG.debug { "Token invalid" }
					return null
				}
			} else {
				LOG.debug { "Authorization header missing" }
				return null
			}
		}

		return message
	}
}