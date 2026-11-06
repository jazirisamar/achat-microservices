package tn.esprit.rh.achat.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.esprit.rh.achat.dto.AuthRequest;
import tn.esprit.rh.achat.entities.AppUser;
import tn.esprit.rh.achat.repositories.AppUserRepository;
import tn.esprit.rh.achat.services.JwtService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

	@Autowired
	AppUserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	JwtService jwtService;

	// POST /auth/register  {"username":"samar","password":"1234"}
	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> register(@RequestBody AuthRequest request) {
		if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", "username et password sont obligatoires"));
		}
		if (userRepository.existsByUsername(request.getUsername())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Collections.singletonMap("error", "utilisateur deja existant"));
		}
		AppUser user = new AppUser();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole("USER");
		userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Collections.singletonMap("message", "utilisateur cree"));
	}

	// POST /auth/login  {"username":"samar","password":"1234"}  ->  {"token":"..."}
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
		Optional<AppUser> found = userRepository.findByUsername(request.getUsername());
		if (!found.isPresent() || request.getPassword() == null
				|| !passwordEncoder.matches(request.getPassword(), found.get().getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Collections.singletonMap("error", "identifiants invalides"));
		}
		String token = jwtService.generateToken(found.get().getUsername(), found.get().getRole());
		return ResponseEntity.ok(Collections.singletonMap("token", token));
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}
