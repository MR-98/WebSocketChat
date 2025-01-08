package com.mr.websocket_chat.service

import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
class WebSocketUtils {

	fun getUsernameFromHeaderAccessor(headerAccessor: SimpMessageHeaderAccessor) =
		(headerAccessor.user as JwtAuthenticationToken).token.claims["preferred_username"]
}