package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.exception.ChatRoomNotFoundException
import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.jpa.ChatMessageEntity
import com.mr.websocket_chat.domain.mapper.ChatMessageMapper
import com.mr.websocket_chat.domain.rest.ChatMessageDTO
import com.mr.websocket_chat.domain.rest.ChatMessageToSaveDTO
import com.mr.websocket_chat.repository.ChatMessageRepository
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Limit
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChatMessageService @Autowired constructor(
	private val chatMessageRepository: ChatMessageRepository,
	private val chatRoomRepository: ChatRoomRepository,
	private val userRepository: UserRepository
) {

	fun saveMessage(chatMessage: ChatMessageToSaveDTO): ChatMessageDTO {
		val room = chatRoomRepository.getReferenceById(chatMessage.roomId)
		val sender = userRepository.findByUsername(chatMessage.senderUsername)
			?: throw UserNotFoundException()
		val messageEntity = chatMessageRepository.save(
			ChatMessageEntity(
                data = chatMessage.data,
                room = room,
                sender = sender,
                timestamp = chatMessage.timestamp
            )
		)
		return ChatMessageMapper.toDTO(messageEntity)
	}

	fun loadNewestMessagesForRoom(roomId: Long): List<ChatMessageDTO> {
		return chatMessageRepository.getAllByRoom_IdOrderByTimestampDesc(
			roomId,
			Limit.of(50)
		).map {
			ChatMessageMapper.toDTO(it)
		}
	}

	fun loadOlderMessagesForRoom(
		loadingUserUsername: String,
		roomId: Long,
		olderThanMessageId: Long,
		numberOfMessagesToLoad: Int
	): List<ChatMessageDTO> {
		val chatRoomEntity = chatRoomRepository.findByIdOrNull(roomId) ?: throw ChatRoomNotFoundException()
		val loadingUserEntity = userRepository.findByUsername(loadingUserUsername) ?: throw UserNotFoundException()
		if(!chatRoomEntity.users.contains(loadingUserEntity)) {
			throw RuntimeException("User cannot load old messages for a room they do not belong to. User id: ${loadingUserUsername}, room id: $roomId.")
		}

		return chatMessageRepository.getAllByRoom_IdAndIdLessThanOrderByTimestampDesc(
			roomId,
			olderThanMessageId,
			Limit.of(numberOfMessagesToLoad)
		).map {
			ChatMessageMapper.toDTO(it)
		}
	}
}