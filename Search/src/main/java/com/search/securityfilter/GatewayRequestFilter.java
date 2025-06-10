package com.search.securityfilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayRequestFilter extends OncePerRequestFilter {

    private static final String GATEWAY_TOKEN = "GATEWAY_SECRET_123";
    private static final String INTERNAL_TOKEN = "INTERNAL_SECRET_456";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String gatewayToken = request.getHeader("X-Gateway-Token");
        String internalToken = request.getHeader("X-Internal-Token");

        if (GATEWAY_TOKEN.equals(gatewayToken) || INTERNAL_TOKEN.equals(internalToken)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied: Not authorized");
        }
    }
}

