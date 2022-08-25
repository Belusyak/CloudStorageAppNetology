package diplom.work.diplombackend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
public class LoginInDto {

	@NotEmpty
	String login;

	@NotEmpty
	String password;

	@Builder
	@JsonCreator
	public LoginInDto(@JsonProperty("login") String login,
					  @JsonProperty("password") String password) {
		this.login = login;
		this.password = password;
	}
}
