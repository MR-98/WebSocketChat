package com.mr.websocket_chat.repository

import com.mr.websocket_chat.domain.jpa.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<UserEntity, Long> {
	fun findByUsername(username: String): UserEntity?

	@Query(value = "SELECT * FROM users WHERE LOWER(CONCAT(first_name, ' ', last_name)) LIKE LOWER(CONCAT('%', :query, '%'))", nativeQuery = true)
	fun findByFullName(@Param("query") query: String): List<UserEntity>
}