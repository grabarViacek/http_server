package study.httpserver.io.exception;

public class HttpVersionNotSupportedException extends AbstractRequestParseFailedExсeption {
	private static final long serialVersionUID = -5282246350498489006L;

	public HttpVersionNotSupportedException(String message, String startingLine) {
		super(message, startingLine);
		setStatusCode(505);
	}

}
