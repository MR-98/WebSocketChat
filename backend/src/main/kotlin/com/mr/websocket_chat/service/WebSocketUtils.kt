package com.mr.websocket_chat.service

import org.springframework.messaging.Message
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Service

@Service
class WebSocketUtils {

    fun getMessageHeaderAccessor(message: Message<*>): StompHeaderAccessor? {
        return MessageHeaderAccessor.getAccessor(
            message,
            StompHeaderAccessor::class.java
        )
    }
}