package jdbc.exceptions;

public class CustomDataSourceException extends RuntimeException {

	public CustomDataSourceException() {
		super();
	}

	public CustomDataSourceException(String message) {
		super(message);
	}

	public CustomDataSourceException(Throwable cause) {
		super(cause);
	}

	public CustomDataSourceException(String message, Throwable cause) {
		super(message, cause);
	}
}