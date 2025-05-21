package com.api.gateway.filter;

import com.api.gateway.service.JWTService;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JWTAuthFilter implements WebFilter {

        @Autowired
        private JWTService jwtUtil;

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            String path = exchange.getRequest().getURI().getPath();

            if (path.startsWith("/security/login")
                    || path.startsWith("/security/register")
                    || path.startsWith("/security/profile/update")
                    || path.startsWith("/search")
                    || path.startsWith("/v3/api-docs")
                    || path.startsWith("/swagger-ui")
                    || path.equals("/swagger-ui.html")) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);
                    List<GrantedAuthority> authorities = jwtUtil.extractAuthorities(token);

                    Authentication auth = new UsernamePasswordAuthenticationToken(email, null, authorities);

                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(builder -> builder
                                    .headers(httpHeaders -> {
                                        httpHeaders.set("X-User-Email", email);
                                        httpHeaders.set(HttpHeaders.AUTHORIZATION, authHeader);
                                    })
                            ).build();


                    // Inject the authentication into the reactive security context
                    return chain.filter(mutatedExchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                }
            }

            // Unauthorized if token missing or invalid
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

}
