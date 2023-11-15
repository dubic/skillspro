package me.skillspro.auth.ratelimiting

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
@Component
class RateLimiterFilter(private val rateLimiterService: RateLimiterService) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(javaClass)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        this.log.debug("Filter ::: {} :::{}", request.servletPath, request.remoteAddr)
        val rateLimitRequest = getIPAndDomain(request)
        val canProceed = this.rateLimiterService.canProceed(rateLimitRequest)
        log.debug("Can proceed ::: {} ::: {}", rateLimitRequest.ip, canProceed)
        if (!canProceed){
            response.status = 429
            response.writer.write("Too many requests")
            response.writer.flush()
            response.writer.close()
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun getIPAndDomain(request: HttpServletRequest): RateLimitRequest {
        if (request.servletPath.matches("/auth/login".toRegex())){
            return RateLimitRequest(request.remoteAddr , request.servletPath)
        }
        throw IllegalStateException("Rate limit filter should not filter: ${request.servletPath}")
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath
        return !path.matches("/auth/login".toRegex())
    }
}