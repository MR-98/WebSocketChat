package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.ChatMessageEntity
import com.mr.websocket_chat.repository.ChatMessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChatMessageService @Autowired constructor(
	private val chatMessageRepository: ChatMessageRepository
) {

	fun saveMessage(chatMessage: ChatMessageEntity) {
		// TODO: add encryption
		chatMessageRepository.save(chatMessage)
	}
}