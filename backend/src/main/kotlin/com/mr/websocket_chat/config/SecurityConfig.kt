package com.mr.websocket_chat.config

import com.mr.websocket_chat.config.jwt.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration


@Configuration
@EnableWebSecurity
class SecurityConfig @Autowired constructor(
	private val jwtAuthenticationFilter: JwtAuthenticationFilter,
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
					}.csrf { csrf -> csrf.disable() }
				}
			}
			.authorizeHttpRequests { authorize ->
				authorize
					.requestMatchers("/api/auth/**").permitAll()
					.requestMatchers("/ws-chat/**").permitAll()
					.requestMatchers("/error").permitAll()
					.anyRequest().authenticated()
			}
			.sessionManagement {session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

		return http.build()
	}
}
