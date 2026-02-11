package com.mr.websocket_chat.controller.rest

import com.mr.websocket_chat.domain.jpa.UserEntity
import com.mr.websocket_chat.domain.rest.UserDTO
import com.mr.websocket_chat.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserRestController @Autowired constructor(
	private val userService: UserService
){

	@GetMapping("/me")
	fun getMe(authentication: Authentication): ResponseEntity<UserEntity> {
		val currentlyAuthenticatedUsername = authentication.name
		val userEntity = userService.findByUsername(currentlyAuthenticatedUsername)
			?: return ResponseEntity.notFound().build()
		return ResponseEntity.ok(userEntity)
	}

	@GetMapping("/search")
	fun searchUsers(@RequestParam query: String): List<UserDTO> {
		return userService.searchUsers(query)
	}
}