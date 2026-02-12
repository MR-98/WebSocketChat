package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.jpa.UserEntity
import com.mr.websocket_chat.domain.mapper.UserMapper
import com.mr.websocket_chat.domain.rest.UserDTO
import com.mr.websocket_chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
	private val userRepository: UserRepository,
	private val authService: AuthService
){

	fun createUser(username: String, firstName: String, lastName: String, password: String): UserDTO {
		val userEntity = userRepository.save(
			UserEntity(
				username = username,
				firstName = firstName,
				lastName = lastName,
				password = authService.encodePassword(password)
			)
		)
		return UserMapper.toDTO(userEntity)
	}

	fun existsByUsername(username: String): Boolean {
		return userRepository.findByUsername(username) != null
	}

	fun findByUsername(username: String): UserDTO {
		val userEntity = userRepository.findByUsername(username) ?: throw UserNotFoundException()
		return UserMapper.toDTO(userEntity)
	}

	fun searchUsers(query: String): List<UserDTO> {
		return userRepository.findByFullName(query).map {
			UserMapper.toDTO(it)
		}
	}
}