package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.jpa.ChatMessageEntity
import com.mr.websocket_chat.domain.rest.ChatMessageDTO
import com.mr.websocket_chat.domain.rest.ChatMessageToSaveDTO
import com.mr.websocket_chat.domain.rest.ChatRoomDTO
import com.mr.websocket_chat.domain.rest.UserDTO
import com.mr.websocket_chat.repository.ChatMessageRepository
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Limit
import org.springframework.stereotype.Service

@Service
class ChatMessageService @Autowired constructor(
	private val chatMessageRepository: ChatMessageRepository,
	private val chatRoomRepository: ChatRoomRepository,
	private val userRepository: UserRepository
) {

	fun saveMessage(chatMessage: ChatMessageToSaveDTO) {
		val room = chatRoomRepository.getReferenceById(chatMessage.roomId)
		val sender = userRepository.findByUsername(chatMessage.senderUsername)
			?: throw UserNotFoundException()
		chatMessageRepository.save(
			ChatMessageEntity(
                data = chatMessage.data,
                room = room,
                sender = sender,
                timestamp = chatMessage.timestamp
            )
		)
	}

	fun loadNewestMessagesForRoom(roomId: Long): List<ChatMessageDTO> {
		return chatMessageRepository.getAllByRoom_IdOrderByTimestampDesc(
			roomId,
			Limit.of(50)
		).map {
			ChatMessageDTO(
				data = it.data,
				room = ChatRoomDTO(
                    it.room.name,
                    users = it.room.users.map { user ->
						UserDTO(
                            username = user.username,
                            firstName = user.firstName,
                            lastName = user.lastName
                        )
					}.toMutableSet(),
                    id = it.id!!,
                ),
				sender = UserDTO(
					username = it.sender.username,
					firstName = it.sender.firstName,
					lastName = it.sender.lastName
				),
				timestamp = it.timestamp,
				id = it.id
			)
		}
	}

	fun loadOlderMessagesForRoom(roomId: Long, olderThanMessageId: Long, numberOfMessagesToLoad: Int): List<ChatMessageDTO> {
		return chatMessageRepository.getAllByRoom_IdAndIdLessThanOrderByTimestampDesc(
			roomId,
			olderThanMessageId,
			Limit.of(numberOfMessagesToLoad)
		).map {
			ChatMessageDTO(
				data = it.data,
				room = ChatRoomDTO(
					it.room.name,
					users = it.room.users.map { user ->
						UserDTO(
							username = user.username,
							firstName = user.firstName,
							lastName = user.lastName
						)
					}.toMutableSet(),
					id = it.id!!,
				),
				sender = UserDTO(
					username = it.sender.username,
					firstName = it.sender.firstName,
					lastName = it.sender.lastName
				),
				timestamp = it.timestamp,
				id = it.id
			)
		}
	}
}