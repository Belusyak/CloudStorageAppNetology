package diplom.work.diplombackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AuthTokenOutDto {

	@JsonProperty("auth-token")
	String authToken;
}
