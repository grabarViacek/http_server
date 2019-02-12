package study.httpserver.io;

import java.util.Map;

public interface HttpRequest {

	String getStartingLine();

	String getMethod();

	String getUri();

	String getHttpVersion();

	Map<String, String> getHeaders();

	Map<String, String> getParameters();

	String getRemoteAddress();

}
