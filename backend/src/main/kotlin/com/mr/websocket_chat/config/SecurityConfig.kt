package com.mr.websocket_chat.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration


@Configuration
class SecurityConfig @Autowired constructor(
	@Value("\${production.environment}") private val productionEnvironment: Boolean,
){

	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http.also {
				if(!productionEnvironment) {
					it.cors { httpSecurityCorsConfigurer ->
						httpSecurityCorsConfigurer.configurationSource {
							val configuration = CorsConfiguration()
							configuration.applyPermitDefaultValues()
							configuration.addAllowedMethod(HttpMethod.DELETE)
							configuration.addAllowedMethod(HttpMethod.PATCH)
							configuration
						}
					}
				}
			}
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
