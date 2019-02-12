package study.httpserver.io.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import study.httpserver.io.HttpHandler;
import study.httpserver.io.HttpRequest;
import study.httpserver.io.HttpResponse;
import study.httpserver.io.HttpServerContext;
import study.httpserver.io.config.HttpRequestDispatcher;
import study.httpserver.io.exception.HttpServerException;

public class DefaultHttpRequestDispatcher implements HttpRequestDispatcher {
	private final HttpHandler defaultHttpHandler;
	private final Map<String, HttpHandler> httpHandlers;

	DefaultHttpRequestDispatcher(HttpHandler defaultHttpHandler, Map<String, HttpHandler> httpHandlers) {
		super();
		Objects.requireNonNull(defaultHttpHandler, "Default handler should be not null");
		Objects.requireNonNull(httpHandlers, "httpHandlers should be not null");
		this.defaultHttpHandler = defaultHttpHandler;
		this.httpHandlers = httpHandlers;
	}

	@Override
	public void handle(HttpServerContext context, HttpRequest request, HttpResponse response) throws IOException {
		try {
			HttpHandler handler = getHttpHandler(request);
			handler.handle(context, request, response);
		} catch (RuntimeException e) {
			if (e instanceof HttpServerException) {
				throw e;
			} else {
				throw new HttpServerException("Handle request: " + request.getUri() + " failed: " + e.getMessage(), e);
			}
		}
	}

	protected HttpHandler getHttpHandler(HttpRequest request) {
		HttpHandler handler = httpHandlers.get(request.getUri());
		if (handler == null) {
			handler = defaultHttpHandler;
		}
		return handler;
	}
}
