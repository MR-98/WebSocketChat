package com.mr.websocket_chat.scheduled

import com.mr.websocket_chat.domain.jpa.ChatMessageEntity
import com.mr.websocket_chat.domain.jpa.ChatRoomEntity
import com.mr.websocket_chat.domain.jpa.InvitationEntity
import com.mr.websocket_chat.domain.jpa.UserEntity
import com.mr.websocket_chat.repository.ChatMessageRepository
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.repository.InvitationRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.util.*

@Component
class DatabaseCleaner @Autowired constructor(
	private val chatRoomRepository: ChatRoomRepository,
	private val invitationRepository: InvitationRepository,
	private val chatMessageRepository: ChatMessageRepository
){

	companion object {
		private val LOG = KotlinLogging.logger{}
		private val firstUser = UserEntity(
			"user1",
			"Jan"	,
			"Kowalski"
		)
		private val secondUser = UserEntity(
			"user2",
			"Adam",
			"Nowak"
		)
		private val thirdUser = UserEntity(
			"user3",
			"Anna",
			"Testowa"
		)
		private val firstRoom = ChatRoomEntity(
			"Pokój 1",
			users = mutableSetOf(
				firstUser,
				secondUser
			),
			id = 0
		)
		private val secondRoom = ChatRoomEntity(
			"Pokój 2",
			users = mutableSetOf(
				firstUser,
				thirdUser
			),
			id = 0
		)
		private val thirdRoom = ChatRoomEntity(
			"Pokój 3",
			users = mutableSetOf(
				secondUser,
				thirdUser
			),
			id = 0
		)
		private val firstInvitation = InvitationEntity(
			firstRoom,
			thirdUser,
			firstUser
		)
		private val secondInvitation = InvitationEntity(
			secondRoom,
			secondUser,
			firstUser
		)
		private val thirdInvitation = InvitationEntity(
			thirdRoom,
			firstUser,
			secondUser
		)
		private val firstMessage = ChatMessageEntity(
			"Bardzo podoba mi się ta aplikacja 💗",
			firstRoom,
			firstUser,
			Timestamp(Date().time - 3 * 60 * 1000) // subtract 3 minutes
		)
		private val secondMessage = ChatMessageEntity(
			"Mi też, powinniśmy zatrudnić developera który ją napisał 👍",
			firstRoom,
			secondUser,
			Timestamp(Date().time)
		)
		private val thirdMessage = ChatMessageEntity(
			"Testowa wiadomość 😀",
			secondRoom,
			firstUser,
			Timestamp(Date().time - 3 * 60 * 1000) // subtract 3 minutes
		)
		private val fourthMessage = ChatMessageEntity(
			"Testowa odpowiedź 😅",
			secondRoom,
			thirdUser,
			Timestamp(Date().time)
		)
		private val fifthMessage = ChatMessageEntity(
			"Cześć, jak się masz?",
			thirdRoom,
			secondUser,
			Timestamp(Date().time - 3 * 60 * 1000) // subtract 3 minutes
		)
		private val sixthMessage = ChatMessageEntity(
			"Cześć, dobrze, a Ty?",
			thirdRoom,
			thirdUser,
			Timestamp(Date().time)
		)
	}

	@Scheduled(cron = "0 0/5 * * * *")
	fun initDatabaseCleanup() {
		LOG.info { "===DATABASE CLEANUP START===" }
		cleanRoomsTable()

		initRoomsTable()
		initInvitations()
		initMessages()
		LOG.info { "===DATABASE CLEANUP FINISH===" }
	}

	private fun cleanRoomsTable() {
		chatRoomRepository.deleteAll()
	}

	private fun initRoomsTable() {
		chatRoomRepository.saveAll(
			listOf(
				firstRoom,
				secondRoom,
				thirdRoom
			)
		)
	}

	private fun initInvitations() {
		invitationRepository.saveAll(
			listOf(
				firstInvitation,
				secondInvitation,
				thirdInvitation
			)
		)
	}

	private fun initMessages() {
		chatMessageRepository.saveAll(
			listOf(
				firstMessage,
				secondMessage,
				thirdMessage,
				fourthMessage,
				fifthMessage,
				sixthMessage
			)
		)
	}
}