package springbook.user.exception;

public class DuplicateUserIdException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateUserIdException(Throwable cause){
		super(cause);
	}
}
