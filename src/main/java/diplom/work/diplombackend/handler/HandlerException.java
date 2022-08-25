package diplom.work.diplombackend.handler;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Response> handleException(RuntimeException e) {
		Response response = new Response(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Value
	public class Response {
		String message;
	}
}
