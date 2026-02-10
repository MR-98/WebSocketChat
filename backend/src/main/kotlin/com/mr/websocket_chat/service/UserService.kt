package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.jpa.UserEntity
import com.mr.websocket_chat.domain.rest.UserDTO
import com.mr.websocket_chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
	private val userRepository: UserRepository
){

	fun findByUsername(username: String): UserEntity? {
		return userRepository.findByUsername(username)
	}

	fun searchUsers(query: String): List<UserDTO> {
		return userRepository.findByFullName(query).map {
			UserDTO(
				username = it.username,
				firstName = it.firstName,
				lastName = it.lastName
			)
		}
	}
}