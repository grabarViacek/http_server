package devstudy.httpserver.io.exeption;

public class HttpServerConfigException extends HttpServerException {

	private static final long serialVersionUID = 1157381644726074078L;

	public HttpServerConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpServerConfigException(String message) {
		super(message);
	}

	public HttpServerConfigException(Throwable cause) {
		super(cause);
	}

}
