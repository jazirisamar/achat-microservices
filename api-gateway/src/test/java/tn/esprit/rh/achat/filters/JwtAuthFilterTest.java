package tn.esprit.rh.achat.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import reactor.core.publisher.Mono;

class JwtAuthFilterTest {

	private final JwtAuthFilter filter =
			new JwtAuthFilter("test-secret-test-secret-test-secret-0123", true);

	@Test
	void sansToken_retourne401() {
		MockServerWebExchange exchange =
				MockServerWebExchange.from(MockServerHttpRequest.get("/stock/retrieve-all-stocks").build());

		filter.filter(exchange, e -> Mono.empty()).block();

		assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
	}

	@Test
	void routeAuth_estLibre() {
		MockServerWebExchange exchange =
				MockServerWebExchange.from(MockServerHttpRequest.post("/auth/login").build());

		filter.filter(exchange, e -> Mono.empty()).block();

		assertEquals(null, exchange.getResponse().getStatusCode());
	}
}
