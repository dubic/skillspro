package me.skillspro.core.config

import me.skillspro.auth.AuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val authenticationFilter: AuthenticationFilter) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .csrf { it.disable() }
                .exceptionHandling {
                    it.authenticationEntryPoint { req, resp, authException ->
                        resp.sendError(HttpStatus.UNAUTHORIZED.value(), "Secured URL Hit")
                    }
                }
                .authorizeHttpRequests {
                    it.requestMatchers("/users/**",
                            "/auth/**", "/engagements/**", "/notification/**").permitAll()
                }
                .headers { h -> h.frameOptions { it.deny() } }
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}