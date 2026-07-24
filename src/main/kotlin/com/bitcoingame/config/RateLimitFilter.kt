package com.bitcoingame.config

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Component
class RateLimitFilter : OncePerRequestFilter() {

    private val buckets = ConcurrentHashMap<String, Bucket>()

    private fun newBucket(): Bucket {
        val limit = Bandwidth.classic(30, io.github.bucket4j.Refill.greedy(30, Duration.ofMinutes(1)))
        return Bucket.builder().addLimit(limit).build()
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!request.requestURI.startsWith("/game/")) {
            filterChain.doFilter(request, response)
            return
        }

        val clientIp = request.remoteAddr
        val bucket = buckets.computeIfAbsent(clientIp) { newBucket() }

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response)
        } else {
            response.status = 429
            response.contentType = "text/html; charset=UTF-8"
            response.writer.write("""
                <div style="padding:2rem; text-align:center; font-family:sans-serif;">
                    <h2>⏳ Calma lá!</h2>
                    <p>Você fez muitas ações rápido demais. Espere um minuto e tente de novo.</p>
                </div>
            """.trimIndent())
        }
    }
}