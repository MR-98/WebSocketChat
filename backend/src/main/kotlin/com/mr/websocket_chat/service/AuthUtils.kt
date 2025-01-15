package com.mr.websocket_chat.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class AuthUtils @Autowired constructor(
	private val jwtDecoder: JwtDecoder,
	private val jwtAuthenticationConverter: JwtAuthenticationConverter
){

	fun getCurrentlyAuthenticatedUsername(): String? {
		return (SecurityContextHolder.getContext().authentication.principal as Jwt).getClaimAsString("preferred_username")
	}

	fun getPrincipalFromAuthorizationHeader(headerAccessor: StompHeaderAccessor): Principal? {
		val authHeader = headerAccessor.getNativeHeader("Authorization")?.firstOrNull()
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			val token = authHeader.substringAfter("Bearer ")
			return try {
				getPrincipalFromToken(token)
			} catch (e: JwtException) {
				null
			}
		} else {
			return null
		}
	}

	fun getPrincipalFromToken(token: String) =
		jwtAuthenticationConverter.convert(jwtDecoder.decode(token)) as Principal

	fun getUsernameFromPrincipal(principal: Principal) =
		(principal as JwtAuthenticationToken).token.claims["preferred_username"] as? String

	fun getUsernameFromToken(token: String) =
		getUsernameFromPrincipal(getPrincipalFromToken(token))
}