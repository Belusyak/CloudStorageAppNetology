package diplom.work.diplombackend.exception;

public class FailLoginException extends RuntimeException{
	public FailLoginException() {
		super("Incorrect login or password");
	}
}
