package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.jpa.ChatMessageEntity
import com.mr.websocket_chat.repository.ChatMessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Limit
import org.springframework.stereotype.Service

@Service
class ChatMessageService @Autowired constructor(
	private val chatMessageRepository: ChatMessageRepository
) {

	fun saveMessage(chatMessage: ChatMessageEntity) {
		chatMessageRepository.save(chatMessage)
	}

	fun loadNewestMessagesForRoom(roomId: Long): List<ChatMessageEntity> {
		return chatMessageRepository.getAllByRoom_IdOrderByTimestampDesc(roomId, Limit.of(50))
	}

	fun loadOlderMessagesForRoom(roomId: Long, olderThanMessageId: Long, numberOfMessagesToLoad: Int): List<ChatMessageEntity> {
		return chatMessageRepository.getAllByRoom_IdAndIdLessThanOrderByTimestampDesc(
			roomId,
			olderThanMessageId,
			Limit.of(numberOfMessagesToLoad)
		)
	}
}