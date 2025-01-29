package com.mr.websocket_chat.config

import com.mr.websocket_chat.interceptor.WebSocketSecurityInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig @Autowired constructor(
	private val webSocketSecurityInterceptor: WebSocketSecurityInterceptor,
	@Value("\${websocket-chat.hostname}") private val hostname: String,
): WebSocketMessageBrokerConfigurer {

	override fun configureMessageBroker(config: MessageBrokerRegistry) {
		config.enableSimpleBroker("/topic")
			.setHeartbeatValue(longArrayOf(0, 30000))
			.setTaskScheduler(heartBeatScheduler())
		config.setApplicationDestinationPrefixes("/app", "/topic")
	}

	override fun registerStompEndpoints(registry: StompEndpointRegistry) {
		registry.addEndpoint("/ws-chat").setAllowedOrigins(hostname)
	}

	override fun configureClientInboundChannel(registration: ChannelRegistration) {
		registration.interceptors(webSocketSecurityInterceptor)
	}

	@Bean
	fun heartBeatScheduler(): TaskScheduler {
		return ThreadPoolTaskScheduler()
	}
}