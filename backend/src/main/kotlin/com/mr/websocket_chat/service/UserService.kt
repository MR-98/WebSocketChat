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

	fun createUser(username: String, firstName: String, lastName: String, encodedPassword: String): UserEntity {
		val user = UserEntity(
			username = username,
			firstName = firstName,
			lastName = lastName,
			password = encodedPassword
		)
		userRepository.save(user)
		return user
	}

	fun existsByUsername(username: String): Boolean {
		return userRepository.findByUsername(username) != null
	}

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