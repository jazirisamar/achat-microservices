package tn.esprit.rh.achat.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class JwtServiceTest {

	private final JwtService jwtService =
			new JwtService("test-secret-test-secret-test-secret-0123", 60000);

	@Test
	void generateToken_puis_extractUsername() {
		String token = jwtService.generateToken("samar", "USER");

		assertNotNull(token);
		assertEquals("samar", jwtService.extractUsername(token));
	}
}
