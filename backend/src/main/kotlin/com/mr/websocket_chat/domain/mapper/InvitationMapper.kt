package com.mr.websocket_chat.domain.mapper

import com.mr.websocket_chat.domain.jpa.InvitationEntity
import com.mr.websocket_chat.domain.rest.InvitationDTO

object InvitationMapper {

    fun toDTO(invitationEntity: InvitationEntity): InvitationDTO {
        return InvitationDTO(
            room = ChatRoomMapper.toDTO(invitationEntity.room),
            invitedUser = UserMapper.toDTO(invitationEntity.invitedUser),
            invitingUser = UserMapper.toDTO(invitationEntity.invitingUser),
            id = invitationEntity.id!!
        )
    }
}