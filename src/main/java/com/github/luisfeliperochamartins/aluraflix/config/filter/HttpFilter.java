package com.github.luisfeliperochamartins.aluraflix.config.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.github.luisfeliperochamartins.aluraflix.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class HttpFilter extends OncePerRequestFilter {

	private final UserRepository repository;

	@Autowired
	public HttpFilter(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		var path = request.getServletPath();

		if (path.startsWith("/videos") || path.startsWith("/categorias")) {
			var pswd = getAuthHeader(request);
			var credentials = decodePassword(pswd);

			var username = credentials[0];
			var password = credentials[1];

			var user = repository.findByUsername(username);
			if (user == null) {
				response.sendError(401, "Usuário sem Autorização");
			} else {
				var senha = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

				if (senha.verified) {
					filterChain.doFilter(request, response);
				} else {
					response.sendError(401, "Usuário sem Autorização");
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	private String getAuthHeader(HttpServletRequest request) {
		var auth = request.getHeader("Authorization");

		if (auth != null) {
			return auth.replace("Basic ", "");
		}
		return null;
	}

	private String[] decodePassword(String encodedPassword) {
		var authDecoded = Base64.getDecoder().decode(encodedPassword);
		var authString = new String(authDecoded);
		return authString.split(":");
	}
}
