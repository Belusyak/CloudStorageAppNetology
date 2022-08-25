package diplom.work.diplombackend.service;

import diplom.work.diplombackend.dto.AuthTokenOutDto;
import diplom.work.diplombackend.dto.LoginInDto;
import diplom.work.diplombackend.exception.ExistsUserException;
import diplom.work.diplombackend.exception.FailLoginException;
import diplom.work.diplombackend.exception.UserNotFoundException;
import diplom.work.diplombackend.model.User;
import diplom.work.diplombackend.repository.UserDao;
import diplom.work.diplombackend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserDao userDao;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Transactional
	public AuthTokenOutDto login(LoginInDto dto) {
		String username = dto.getLogin();
		String password = dto.getPassword();
		User user = userDao.findByUsername(username).orElseThrow(UserNotFoundException::new);
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new FailLoginException();
		}
		return new AuthTokenOutDto(jwtTokenProvider.createToken(user.getUsername(), user.getRoles()));
	}

	@Transactional
	public void signIn(LoginInDto dto) {
		if (userDao.existsByUsername(dto.getLogin())) {
			throw new ExistsUserException();
		} else {
			userDao.save(new User().setUsername(dto.getLogin()).setPassword(passwordEncoder.encode(dto.getPassword())));
		}
	}

}
