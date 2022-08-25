package diplom.work.diplombackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import diplom.work.diplombackend.configuration.SecurityConfig;
import diplom.work.diplombackend.model.Role;
import diplom.work.diplombackend.model.User;
import diplom.work.diplombackend.repository.RoleDao;
import diplom.work.diplombackend.repository.UserDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = {JpaTestConfig.class, DiplomBackendApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityConfig.class)
@MockBean(classes = {
		AuthenticationManager.class
})
public abstract class BaseTest {

	@Value("${token.key}")
	private String TOKEN_KEY;
	@Value("${token.expiration_date}")
	private int TOKEN_EXPIRATION_PERIOD;
	@Value("${token.key}")
	private String secret;

	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	protected UserDao userDao;
	@Autowired
	protected RoleDao roleDao;
	@Autowired
	protected BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


	public String toJson(@NonNull Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	protected User createUser() {
		Role role = roleDao.save(new Role().setName("TEST_USER"));
		return userDao.save(new User().setUsername("loginTest").setPassword(passwordEncoder.encode("password")).setRoles(List.of(role)));
	}

	protected String getToken(User user) {
		secret = Base64.getEncoder().encodeToString(secret.getBytes());
		Claims claims = Jwts.claims().setSubject(user.getUsername());
		claims.put("roles", getRoleNames(user.getRoles()));
		Date now = new Date();
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + TOKEN_EXPIRATION_PERIOD))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	private List<String> getRoleNames(List<Role> roles) {
		return roles.stream().map(Role::getName).collect(Collectors.toUnmodifiableList());
	}
}
