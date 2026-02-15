package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.domain.jpa.UserEntity
import com.mr.websocket_chat.domain.mapper.UserMapper
import com.mr.websocket_chat.domain.rest.UserDTO
import com.mr.websocket_chat.repository.UserRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService @Autowired constructor(
	private val userRepository: UserRepository,
	private val authService: AuthService,
	private val userMapper: UserMapper
){

	companion object {
		private val LOG = KotlinLogging.logger{}
	}

	@Transactional
	fun createUser(username: String, firstName: String, lastName: String, password: String): UserDTO {
		val userEntity = userRepository.save(
			UserEntity(
				username = username,
				firstName = firstName,
				lastName = lastName,
				password = authService.encodePassword(password)
			)
		)
		return userMapper.toDTO(userEntity)
	}

	@Transactional(readOnly = true)
	fun existsByUsername(username: String): Boolean {
		return userRepository.findByUsername(username) != null
	}

	@Transactional(readOnly = true)
	fun findByUsername(username: String): UserDTO {
		LOG.debug { "Searching for user: $username" }
		val userEntity = userRepository.findByUsername(username) ?: throw UserNotFoundException()
		return userMapper.toDTO(userEntity)
	}

	@Transactional(readOnly = true)
	fun searchUsers(query: String): List<UserDTO> {
		return userRepository.findByFullName(query).map {
			userMapper.toDTO(it)
		}
	}
}