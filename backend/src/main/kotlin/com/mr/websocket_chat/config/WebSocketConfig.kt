package com.mr.websocket_chat.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig @Autowired constructor(
	private val wsChannelInterceptor: WSChannelInterceptor
): WebSocketMessageBrokerConfigurer {

	override fun configureMessageBroker(config: MessageBrokerRegistry) {
		config.enableSimpleBroker("/topic")
		config.setApplicationDestinationPrefixes("/app")
	}

	override fun registerStompEndpoints(registry: StompEndpointRegistry) {
		registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*")
	}

	override fun configureClientInboundChannel(registration: ChannelRegistration) {
		registration.interceptors(wsChannelInterceptor)
	}
}