package jdbc.exceptions;

public class SimpleJDBCRepositoryException extends RuntimeException {

	public SimpleJDBCRepositoryException() {
		super();
	}

	public SimpleJDBCRepositoryException(String message) {
		super(message);
	}

	public SimpleJDBCRepositoryException(Throwable cause) {
		super(cause);
	}

	public SimpleJDBCRepositoryException(String message, Throwable cause) {
		super(message, cause);
	}
}