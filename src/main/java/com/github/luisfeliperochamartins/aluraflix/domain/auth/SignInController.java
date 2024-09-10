package com.github.luisfeliperochamartins.aluraflix.domain.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.github.luisfeliperochamartins.aluraflix.domain.user.User;
import com.github.luisfeliperochamartins.aluraflix.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/sign-in")
public class SignInController {

	private final UserRepository repository;

	@Autowired
	public SignInController(UserRepository repository) {
		this.repository = repository;
	}

	@PostMapping
	public ResponseEntity<User> register(@RequestBody User body, UriComponentsBuilder uriBuilder) {
		var password = BCrypt.withDefaults()
				.hashToString(12, body.getPassword().toCharArray());

		body.setPassword(password);

		var user = repository.save(body);

		var uri = uriBuilder.path("/sign-in/{id}").buildAndExpand(user.getId()).toUri();

		return ResponseEntity.created(uri).body(user);
	}
}
