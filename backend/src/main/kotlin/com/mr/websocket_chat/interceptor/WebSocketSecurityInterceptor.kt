package com.mr.websocket_chat.interceptor

import com.mr.websocket_chat.interceptor.factory.WebSocketMessageHandlerFactory
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component

@Component
class WebSocketSecurityInterceptor @Autowired constructor(
	private val webSocketMessageHandlerFactory: WebSocketMessageHandlerFactory
) : ChannelInterceptor {

	companion object {
		private val LOG = KotlinLogging.logger{}
		private const val MESSAGE_HANDLER_SUFFIX = "Handler"
	}

	override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
		val headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java) ?: return null
		val messageType = headerAccessor.messageType?.name?.lowercase() ?: return null
		val handler = webSocketMessageHandlerFactory.getHandler(messageType + MESSAGE_HANDLER_SUFFIX)
			?: return message
		return handler.handleMessage(message)
	}
}