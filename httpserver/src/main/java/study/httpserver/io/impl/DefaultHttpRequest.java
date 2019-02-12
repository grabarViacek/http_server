package study.httpserver.io.impl;

import java.util.Collections;
import java.util.Map;

import study.httpserver.io.HttpRequest;

class DefaultHttpRequest implements HttpRequest {
	private final String removedAdress;
	private final String method;
	private final String uri;
	private final String httpVersion;
	private final Map<String, String> headers;
	private final Map<String, String> parameters;

	DefaultHttpRequest(String removedAdress, String method, String uri, String httpVersion, Map<String, String> headers,
			Map<String, String> parameters) {
		super();
		this.removedAdress = removedAdress;
		this.method = method;
		this.uri = uri;
		this.httpVersion = httpVersion;
		this.headers = Collections.unmodifiableMap(headers);
		this.parameters = Collections.unmodifiableMap(parameters);
	}

	@Override
	public String getStartingLine() {
		return String.format("%s %s %s", method, uri, httpVersion);
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public String getHttpVersion() {
		return httpVersion;
	}

	@Override
	public String getRemoteAddress() {
		return removedAdress;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public Map<String, String> getParameters() {
		return parameters;
	}

}
