package com.mr.websocket_chat.repository

import com.mr.websocket_chat.domain.InvitationEntity
import org.springframework.data.domain.Limit
import org.springframework.data.jpa.repository.JpaRepository

interface InvitationRepository : JpaRepository<InvitationEntity, Long> {
	fun getAllByInvitedUser_Username(username: String, limit: Limit): List<InvitationEntity>
}