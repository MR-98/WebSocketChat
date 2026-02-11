package com.mr.websocket_chat.controller.rest

import com.mr.websocket_chat.config.jwt.JwtService
import com.mr.websocket_chat.domain.rest.JWTValidateResponseDTO
import com.mr.websocket_chat.domain.rest.LoginRequestDTO
import com.mr.websocket_chat.domain.rest.LoginResponseDTO
import com.mr.websocket_chat.domain.rest.RegisterRequestDTO
import com.mr.websocket_chat.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val jwtService: JwtService,
    private val userService: UserService
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequestDTO): ResponseEntity<String> {
        if (userService.existsByUsername(request.username)) {
            return ResponseEntity
                .badRequest()
                .body("Username is already taken")
        }

        val encodedPassword = passwordEncoder.encode(request.password)
        userService.createUser(request.username, request.firstName, request.lastName, encodedPassword)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<*> {
        val user = userService.findByUsername(request.username)
            ?: return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password")

        if (!passwordEncoder.matches(request.password, user.password)) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password")
        }

        val token = jwtService.generateToken(request.username)
        return ResponseEntity.ok(LoginResponseDTO(token, request.username))
    }

    @GetMapping("/validate")
    fun validate(@RequestHeader("Authorization") authHeader: String): ResponseEntity<JWTValidateResponseDTO> {
        val token = jwtService.extractTokenFromHeader(authHeader)
            ?: return ResponseEntity.badRequest().body(JWTValidateResponseDTO(false, null))

        val isValid = jwtService.validateToken(token)
        val username = if (isValid) jwtService.getUsernameFromToken(token) else null

        return ResponseEntity.ok(JWTValidateResponseDTO(isValid, username))
    }
}