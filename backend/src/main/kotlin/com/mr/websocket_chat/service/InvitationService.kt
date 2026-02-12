package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.exception.ChatRoomNotFoundException
import com.mr.websocket_chat.domain.exception.InvitationNotFoundException
import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.jpa.InvitationEntity
import com.mr.websocket_chat.domain.mapper.ChatRoomMapper
import com.mr.websocket_chat.domain.mapper.InvitationMapper
import com.mr.websocket_chat.domain.rest.ChatRoomDTO
import com.mr.websocket_chat.domain.rest.InvitationDTO
import com.mr.websocket_chat.domain.rest.InvitationToSaveDTO
import com.mr.websocket_chat.repository.ChatRoomRepository
import com.mr.websocket_chat.repository.InvitationRepository
import com.mr.websocket_chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.Limit
import org.springframework.data.repository.findByIdOrNull

@Service
class InvitationService @Autowired constructor(
	private val invitationRepository: InvitationRepository,
	private val chatRoomRepository: ChatRoomRepository,
	private val userRepository: UserRepository,
){
	fun saveInvitation(invitation: InvitationToSaveDTO, invitingUser: String): InvitationDTO {
		val chatRoomEntity = chatRoomRepository.findByIdOrNull(invitation.roomId)
			?: throw ChatRoomNotFoundException()
		val invitedUserEntity = userRepository.findByUsername(invitation.invitedUser)
			?: throw UserNotFoundException()
		val invitingUserEntity = userRepository.findByUsername(invitingUser)
			?: throw UserNotFoundException()

		val invitationEntity = invitationRepository.save(
			InvitationEntity(
				room = chatRoomEntity,
				invitedUser = invitedUserEntity,
				invitingUser = invitingUserEntity
			)
		)
		return InvitationMapper.toDTO(invitationEntity)
	}

	fun loadInvitationsForUser(username: String): List<InvitationDTO> {
		return invitationRepository.getAllByInvitedUser_Username(username, Limit.of(50)).map {
			InvitationMapper.toDTO(it)
		}
	}

	fun findInvitationForRoomAndUser(roomId: Long, username: String): InvitationEntity? {
		return invitationRepository.findByRoom_IdAndInvitedUser_Username(roomId, username)
	}

	fun acceptInvitationAndReturnNewRoom(roomId: Long, username: String): ChatRoomDTO {
		val invitation = findInvitationForRoomAndUser(roomId, username) ?: throw InvitationNotFoundException()
		invitation.room.users.add(invitation.invitedUser)
		val room = chatRoomRepository.save(invitation.room)
		deleteInvitation(invitation)
		return ChatRoomMapper.toDTO(room)
	}

	fun rejectInvitation(roomId: Long, username: String) {
		val invitation = findInvitationForRoomAndUser(roomId, username) ?: throw InvitationNotFoundException()
		deleteInvitation(invitation)
	}

	fun deleteInvitation(invitation: InvitationEntity) {
		invitationRepository.deleteById(invitation.id!!)
	}

}