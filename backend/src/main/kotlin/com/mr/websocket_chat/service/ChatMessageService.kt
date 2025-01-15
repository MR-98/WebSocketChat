package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.ChatMessageEntity
import com.mr.websocket_chat.repository.ChatMessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Limit
import org.springframework.stereotype.Service

@Service
class ChatMessageService @Autowired constructor(
	private val chatMessageRepository: ChatMessageRepository
) {

	fun saveMessage(chatMessage: ChatMessageEntity) {
		// TODO: add encryption
		chatMessageRepository.save(chatMessage)
	}

	fun loadNewestMessagesForRoom(roomId: String): List<ChatMessageEntity> {
		return chatMessageRepository.getAllByRoom_IdOrderByTimestampDesc(roomId.toLong(), Limit.of(50)).reversed()
	}
}