package diplom.work.diplombackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorMessage> resourceNotFoundException(RuntimeException ex) {
		ErrorMessage message = new ErrorMessage(
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage()
		);
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
}
