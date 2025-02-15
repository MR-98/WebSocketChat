package com.mr.websocket_chat.interceptor.handler

import org.springframework.messaging.Message

interface WebSocketMessageHandler {
	fun handleMessage(message: Message<*>): Message<*>?
}