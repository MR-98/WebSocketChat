package com.mr.websocket_chat.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig {

	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.sessionManagement {session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.authorizeHttpRequests { authorize ->
				authorize
					.requestMatchers("/ws-chat/**").permitAll()
					.anyRequest().authenticated()
			}
			.oauth2ResourceServer { oauth2 ->
				oauth2.jwt { jwt ->
					jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
				}
			}
		return http.build()
	}

	@Bean
	fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
		val converter = JwtAuthenticationConverter()
		converter.setJwtGrantedAuthoritiesConverter(KeycloakRoleConverter())
		return converter
	}
}
