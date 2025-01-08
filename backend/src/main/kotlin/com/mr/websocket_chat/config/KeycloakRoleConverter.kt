package com.mr.websocket_chat.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

class KeycloakRoleConverter : Converter<Jwt, Collection<GrantedAuthority>> {

	override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
		val roles = jwt.claims["realm_access"]?.let {
			(it as Map<*, *>)["roles"] as? Collection<String>
		} ?: emptyList()

		return roles.map { role -> SimpleGrantedAuthority("ROLE_$role") }
	}
}
