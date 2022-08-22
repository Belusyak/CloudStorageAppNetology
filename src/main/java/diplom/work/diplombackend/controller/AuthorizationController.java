package diplom.work.diplombackend.controller;

import diplom.work.diplombackend.model.User;
import diplom.work.diplombackend.repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {

	private final UserDao userDao;
	private final AuthenticationManager authenticationManager;

	@PostMapping(value = "/login")
	public String loginUser(String login, String password) {

		Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(identity.getLogin(), identity.getPassword()));

		User user = (User) authenticate.getPrincipal();

		return new jwtTokenAuthenticationService.generateToken(user);
	}
}
