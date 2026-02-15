package com.mr.websocket_chat.domain.mapper

import com.mr.websocket_chat.domain.jpa.InvitationEntity
import com.mr.websocket_chat.domain.rest.InvitationDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class InvitationMapper @Autowired constructor(
    private val chatRoomMapper: ChatRoomMapper,
    private val userMapper: UserMapper
){

    fun toDTO(invitationEntity: InvitationEntity): InvitationDTO {
        return InvitationDTO(
            room = chatRoomMapper.toDTO(invitationEntity.room),
            invitedUser = userMapper.toDTO(invitationEntity.invitedUser),
            invitingUser = userMapper.toDTO(invitationEntity.invitingUser),
            id = invitationEntity.id!!
        )
    }
}