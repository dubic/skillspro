package me.skillspro.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.skillspro.auth.models.UserAuthentication
import me.skillspro.auth.session.SessionService
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Order(10)
@Component
class AuthenticationFilter(private val sessionService: SessionService) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authToken = getAuthToken(request)
        if (authToken == null) {
            log.debug("No bearer token in header")
            filterChain.doFilter(request, response)
            return
        }
        //get user in session
        val user = this.sessionService.userInSession(authToken)
        if (user == null) {
            log.debug("Session does not exist")
            filterChain.doFilter(request, response)
            return
        }
        //set user in thread local
        SecurityContextHolder.getContext().authentication = UserAuthentication(true,
                authToken, user)
        filterChain.doFilter(request, response)
    }

    private fun getAuthToken(request: HttpServletRequest): String? {
        val header = request.getHeader("Authorization") ?: return null
        return header.substringAfter("Bearer ")
    }
}