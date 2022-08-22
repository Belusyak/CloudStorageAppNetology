package diplom.work.diplombackend.service;

import diplom.work.diplombackend.model.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorizationService {

	@Value("token.key")
	private final String tokenKey;

	public TokenObject getToken(String username, String password) throws Exception {
		if (username == null || password == null)
			return null;
		User user = (User) userDetailsService.loadUserByUsername(username);
		Map<String, Object> tokenData = new HashMap<>();
		if (password.equals(user.getPassword())) {
			tokenData.put("userID", user.getUserId().toString());
			tokenData.put("username", authorizedUser.getUsername());

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, 30);
			tokenData.put("token_expiration_date", calendar.getTime());
			JwtBuilder jwtBuilder = Jwts.builder();
			jwtBuilder.setExpiration(calendar.getTime());
			jwtBuilder.setClaims(tokenData);
			String key = "abc123";
			String token = jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
			return token;
		} else {
			throw new Exception("Authentication error");
		}
	}

}

}
