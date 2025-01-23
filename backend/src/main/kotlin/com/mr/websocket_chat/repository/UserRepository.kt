package com.mr.websocket_chat.repository

import com.mr.websocket_chat.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<UserEntity, Long> {
	fun findByUsername(username: String): UserEntity?

	@Query(value = "SELECT * FROM users WHERE concat(first_name, ' ', last_name) LIKE %:query%", nativeQuery = true)
	fun findByFullName(@Param("query") query: String): List<UserEntity>
}