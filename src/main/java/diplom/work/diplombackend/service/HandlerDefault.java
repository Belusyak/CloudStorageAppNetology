package diplom.work.diplombackend.service;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice
@RestControllerAdvice
public class HandlerDefault {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Response> handleException(RuntimeException e) {
		Response response = new Response(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Value
	public static class Response {
		String message;
	}
}
