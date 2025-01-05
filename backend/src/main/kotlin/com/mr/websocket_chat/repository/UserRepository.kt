package com.mr.websocket_chat.repository

import com.mr.websocket_chat.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
	fun findByUsername(username: String): UserEntity?
}