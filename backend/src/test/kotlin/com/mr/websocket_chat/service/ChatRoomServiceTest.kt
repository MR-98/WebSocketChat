package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.exception.ChatRoomNotFoundException
import com.mr.websocket_chat.domain.jpa.ChatRoomEntity
import com.mr.websocket_chat.domain.jpa.UserEntity
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.repository.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test

class ChatRoomServiceTest {

	@MockK
	private val chatRoomRepository: ChatRoomRepository = mockk()

	@MockK
	private val userRepository: UserRepository = mockk()

	@InjectMockKs
	private val chatRoomService: ChatRoomService = ChatRoomService(chatRoomRepository, userRepository)

	private lateinit var chatRoomEntity: ChatRoomEntity

	private lateinit var user1: UserEntity

	private lateinit var user2: UserEntity

	@BeforeEach
	fun setUp() {
		user1 = UserEntity(
			username = "username1",
			firstName = "firstName1",
			lastName = "lastName1",
		)
		user2 = UserEntity(
			username = "username2",
			firstName = "firstName2",
			lastName = "lastName2",
		)
		chatRoomEntity = ChatRoomEntity(
			id = 0L,
			name = "Room 1",
			users = mutableSetOf(user1)
		)
	}

	@Test
	fun whenRemovingLastRoomUser_thenDeleteRoom() {
		// given
		every { chatRoomRepository.findByIdOrNull(chatRoomEntity.id) } returns chatRoomEntity
        every { chatRoomRepository.deleteById(chatRoomEntity.id!!) } returns Unit

        // when
		chatRoomService.removeUserFromRoom(chatRoomEntity.id!!, user1.username)

        // then
		verify { chatRoomRepository.deleteById(chatRoomEntity.id!!) }
		verify(inverse = true) { chatRoomRepository.save(chatRoomEntity) }
	}

	@Test
	fun whenRemovingRoomUser_thenUpdateRoom() {
		// given
		chatRoomEntity.users.add(user2)
		every { chatRoomRepository.findByIdOrNull(chatRoomEntity.id) } returns chatRoomEntity
		every { chatRoomRepository.save(chatRoomEntity) } returns chatRoomEntity
		every { chatRoomRepository.deleteById(chatRoomEntity.id!!) } returns Unit

		// when
		chatRoomService.removeUserFromRoom(chatRoomEntity.id!!, user1.username)

		// then
		verify { chatRoomRepository.save(chatRoomEntity) }
		verify(inverse = true) { chatRoomRepository.deleteById(chatRoomEntity.id!!) }
	}

	@Test
	fun whenRemovingUserFromNonExistingRoom_thenThrowException() {
		// given
		every { chatRoomRepository.findByIdOrNull(chatRoomEntity.id) } returns null

		// when
		val action = { chatRoomService.removeUserFromRoom(chatRoomEntity.id!!, user1.username) }

		// then
		assertThrows<ChatRoomNotFoundException> {
			action()
		}
		verify(inverse = true) { chatRoomRepository.save(chatRoomEntity) }
		verify(inverse = true) { chatRoomRepository.deleteById(chatRoomEntity.id!!) }
	}
}