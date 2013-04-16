package springbook.user.sqlservice;

public class SqlNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SqlNotFoundException(String message) {
		super(message);
	}
	
	public SqlNotFoundException(String message, Throwable e){
		super(message, e);
	}
}
