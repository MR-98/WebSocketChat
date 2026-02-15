package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.exception.ChatRoomNotFoundException
import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.jpa.AttachmentEntity
import com.mr.websocket_chat.domain.jpa.ChatMessageEntity
import com.mr.websocket_chat.domain.mapper.ChatMessageMapper
import com.mr.websocket_chat.domain.rest.ChatMessageDTO
import com.mr.websocket_chat.domain.rest.ChatMessageToSaveDTO
import com.mr.websocket_chat.repository.AttachmentRepository
import com.mr.websocket_chat.repository.ChatMessageRepository
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Limit
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatMessageService @Autowired constructor(
	private val chatMessageRepository: ChatMessageRepository,
	private val chatRoomRepository: ChatRoomRepository,
	private val userRepository: UserRepository,
	private val chatMessageMapper: ChatMessageMapper,
	private val attachmentRepository: AttachmentRepository
) {

	@Transactional
	fun saveMessage(chatMessage: ChatMessageToSaveDTO): ChatMessageDTO {
		val room = chatRoomRepository.getReferenceById(chatMessage.roomId)
		val sender = userRepository.findByUsername(chatMessage.senderUsername)
			?: throw UserNotFoundException()

		val messageEntity = ChatMessageEntity(
			data = chatMessage.data,
			room = room,
			sender = sender,
			timestamp = chatMessage.timestamp
		)
		val messageEntityWithAttachments = handleMessageAttachments(chatMessage, messageEntity)
		val savedMessage = chatMessageRepository.save(messageEntityWithAttachments)
		return chatMessageMapper.toDTO(savedMessage)
	}

	@Transactional(readOnly = true)
	fun loadNewestMessagesForRoom(roomId: Long): List<ChatMessageDTO> {
		return chatMessageRepository.getAllByRoom_IdOrderByTimestampDesc(
			roomId,
			Limit.of(50)
		).map {
			chatMessageMapper.toDTO(it)
		}
	}

	@Transactional(readOnly = true)
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
			chatMessageMapper.toDTO(it)
		}
	}

	private fun handleMessageAttachments(
		chatMessage: ChatMessageToSaveDTO,
		messageEntity: ChatMessageEntity
	): ChatMessageEntity {
		if(chatMessage.attachmentIds.isNotEmpty()) {
			val attachmentEntities = findAttachmentsForMessage(chatMessage.attachmentIds)
			messageEntity.attachments = attachmentEntities
			attachmentEntities.forEach {
				it.message = messageEntity
			}
		}
		return messageEntity
	}

	private fun findAttachmentsForMessage(attachmentIds: List<Long>): List<AttachmentEntity> {
		return attachmentRepository.findAllById(attachmentIds)
	}
}