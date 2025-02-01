package com.mr.websocket_chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class WebSocketChatApplication

fun main(args: Array<String>) {
	runApplication<WebSocketChatApplication>(*args)
}
