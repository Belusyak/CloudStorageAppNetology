package diplom.work.diplombackend.exception;

import lombok.Value;

@Value
public class ErrorMessage {
	int statusCode;
	String message;
}
