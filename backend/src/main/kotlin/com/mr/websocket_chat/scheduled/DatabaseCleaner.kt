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
	}

	@Scheduled(cron = "0 0 * * * *")
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
		val firstRoom = ChatRoomEntity(
			"Pokój 1",
			users = mutableSetOf(
				firstUser,
				secondUser
			),
			id = 0
		)
		val secondRoom = ChatRoomEntity(
			"Pokój 2",
			users = mutableSetOf(
				firstUser,
				thirdUser
			),
			id = 0
		)
		val thirdRoom = ChatRoomEntity(
			"Pokój 3",
			users = mutableSetOf(
				secondUser,
				thirdUser
			),
			id = 0
		)
		chatRoomRepository.saveAllAndFlush(
			listOf(
				firstRoom,
				secondRoom,
				thirdRoom
			)
		)
	}

	private fun initInvitations() {
		val firstRoom = chatRoomRepository.findByName("Pokój 1")!!
		val secondRoom = chatRoomRepository.findByName("Pokój 2")!!
		val thirdRoom = chatRoomRepository.findByName("Pokój 3")!!
		val firstInvitation = InvitationEntity(
			firstRoom,
			thirdUser,
			firstUser
		)
		val secondInvitation = InvitationEntity(
			secondRoom,
			secondUser,
			firstUser
		)
		val thirdInvitation = InvitationEntity(
			thirdRoom,
			firstUser,
			secondUser
		)
		invitationRepository.saveAll(
			listOf(
				firstInvitation,
				secondInvitation,
				thirdInvitation
			)
		)
	}

	private fun initMessages() {
		val firstRoom = chatRoomRepository.findByName("Pokój 1")!!
		val secondRoom = chatRoomRepository.findByName("Pokój 2")!!
		val thirdRoom = chatRoomRepository.findByName("Pokój 3")!!
		val firstMessage = ChatMessageEntity(
			"Bardzo podoba mi się ta aplikacja 💗",
			firstRoom,
			firstUser,
			Timestamp(Date().time - 3 * 60 * 1000) // subtract 3 minutes
		)
		val secondMessage = ChatMessageEntity(
			"Mi też, powinniśmy zatrudnić developera który ją napisał 👍",
			firstRoom,
			secondUser,
			Timestamp(Date().time)
		)
		val thirdMessage = ChatMessageEntity(
			"Testowa wiadomość 😀",
			secondRoom,
			firstUser,
			Timestamp(Date().time - 3 * 60 * 1000) // subtract 3 minutes
		)
		val fourthMessage = ChatMessageEntity(
			"Testowa odpowiedź 😅",
			secondRoom,
			thirdUser,
			Timestamp(Date().time)
		)
		val fifthMessage = ChatMessageEntity(
			"Cześć, jak się masz?",
			thirdRoom,
			secondUser,
			Timestamp(Date().time - 3 * 60 * 1000) // subtract 3 minutes
		)
		val sixthMessage = ChatMessageEntity(
			"Cześć, dobrze, a Ty?",
			thirdRoom,
			thirdUser,
			Timestamp(Date().time)
		)
		chatMessageRepository.saveAllAndFlush(
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