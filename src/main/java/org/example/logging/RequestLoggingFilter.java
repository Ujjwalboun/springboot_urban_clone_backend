package org.example.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger("RequestLogger");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String timestamp = LocalDateTime.now().format(formatter);
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
            int status = response.getStatus();
            String userAgent = request.getHeader("User-Agent");
            String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";

            logger.info("[{}] {} {} {} {} - {} ms - {} - {}",
                    timestamp,
                    method,
                    uri + queryString,
                    status,
                    username,
                    duration,
                    userAgent,
                    request.getRemoteAddr());

            responseWrapper.copyBodyToResponse();
        }
    }
}