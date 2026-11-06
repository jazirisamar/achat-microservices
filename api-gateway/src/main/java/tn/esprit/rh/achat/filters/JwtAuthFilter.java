package tn.esprit.rh.achat.filters;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

/**
 * Verifie le jeton JWT (emis par auth-service) sur toutes les routes,
 * sauf /auth/** (login et inscription).
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

	private final String secret;
	private final boolean enabled;

	public JwtAuthFilter(@Value("${jwt.secret}") String secret,
			@Value("${gateway.auth.enabled:true}") boolean enabled) {
		this.secret = secret;
		this.enabled = enabled;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		if (!enabled || path.startsWith("/auth/") || path.startsWith("/actuator")) {
			return chain.filter(exchange);
		}
		String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (header == null || !header.startsWith("Bearer ")) {
			return unauthorized(exchange);
		}
		try {
			Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
					.build()
					.parseClaimsJws(header.substring(7));
			return chain.filter(exchange);
		} catch (JwtException | IllegalArgumentException e) {
			return unauthorized(exchange);
		}
	}

	private Mono<Void> unauthorized(ServerWebExchange exchange) {
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return exchange.getResponse().setComplete();
	}

	@Override
	public int getOrder() {
		return -1;
	}
}
