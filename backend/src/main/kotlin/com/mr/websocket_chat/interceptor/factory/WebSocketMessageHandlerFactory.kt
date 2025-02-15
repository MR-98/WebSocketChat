package com.mr.websocket_chat.interceptor.factory

import com.mr.websocket_chat.interceptor.handler.WebSocketMessageHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class WebSocketMessageHandlerFactory {

	@Autowired
	private lateinit var paymentProviderMap: Map<String, WebSocketMessageHandler>

	fun getHandler(handlerName: String): WebSocketMessageHandler? {
		return paymentProviderMap[handlerName]
	}
}