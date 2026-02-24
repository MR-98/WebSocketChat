package com.mr.websocket_chat.service

import com.mr.websocket_chat.domain.exception.UserNotFoundException
import com.mr.websocket_chat.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService @Autowired constructor(
    private val userRepository: UserRepository
){

    private val passwordEncoder = BCryptPasswordEncoder()

    @Transactional(readOnly = true)
    fun checkIfPasswordValid(username: String, password: String): Boolean {
        val userEntity = userRepository.findByUsername(username) ?: throw UserNotFoundException()
        return passwordEncoder.matches(password, userEntity.password)
    }

    fun encodePassword(password: String): String = passwordEncoder.encode(password)
}