package diplom.work.diplombackend.controller;

import diplom.work.diplombackend.dto.AuthTokenOutDto;
import diplom.work.diplombackend.dto.LoginInDto;
import diplom.work.diplombackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public AuthTokenOutDto loginUser(@RequestBody @Valid LoginInDto dto) {
		return authService.login(dto);
	}

	@PostMapping("/sign-in")
	public void signIn(@RequestBody @Valid LoginInDto dto) {
		authService.signIn(dto);
	}

}
