package devstudy.httpserver.io.exeption;

public abstract class AbstractRequestParseFailedExсeption extends HttpServerException {
	private static final long serialVersionUID = 5763295926348755161L;
	private final String startingLine;

	public AbstractRequestParseFailedExсeption(String message, Throwable cause, String startingLine) {
		super(message, cause);
		this.startingLine = startingLine;
	}

	public AbstractRequestParseFailedExсeption(String message, String startingLine) {
		super(message);
		this.startingLine = startingLine;
	}

	public AbstractRequestParseFailedExсeption(Throwable cause, String startingLine) {
		super(cause);
		this.startingLine = startingLine;
	}

	public String getStartingLine() {
		return startingLine;
	}

}
