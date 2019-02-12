package study.httpserver.io.exception;

public class BadRequestException extends AbstractRequestParseFailedExсeption {
	private static final long serialVersionUID = 3024137206168127950L;

	public BadRequestException(String message, Throwable cause, String startingLine) {
		super(message, cause, startingLine);
		setStatusCode(400);
	}
}
