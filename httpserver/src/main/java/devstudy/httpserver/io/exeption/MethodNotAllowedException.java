package devstudy.httpserver.io.exeption;

import devstudy.httpserver.io.Constants;

public class MethodNotAllowedException extends AbstractRequestParseFailedExсeption {

	private static final long serialVersionUID = 5350208346887918106L;

	public MethodNotAllowedException(String method, String startingLine) {
		super("Only " + Constants.ALLOWED_METHODS + " are supported. Current method is " + method, startingLine);
		setStatusCode(405);
	}
}
