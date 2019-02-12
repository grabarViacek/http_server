package study.httpserver.io.impl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import study.httpserver.io.Constants;
import study.httpserver.io.HttpRequest;
import study.httpserver.io.HttpResponse;
import study.httpserver.io.HttpServerContext;
import study.httpserver.io.config.HttpClientSocketHandler;
import study.httpserver.io.config.HttpServerConfig;
import study.httpserver.io.config.ReadableHttpResponse;
import study.httpserver.io.exception.AbstractRequestParseFailedExсeption;
import study.httpserver.io.exception.HttpServerException;
import study.httpserver.io.exception.MethodNotAllowedException;

public class DefaultHttpClientSocketHandler implements HttpClientSocketHandler {
	private static final Logger ACCESS_LOGGER = LoggerFactory.getLogger("ACCESS_LOG");
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpClientSocketHandler.class);
	private final Socket clientSocket;
	private final String remoteAddress;
	private final HttpServerConfig httpServerConfig;

	DefaultHttpClientSocketHandler(Socket clientSocket, HttpServerConfig httpServerConfig) {
		super();
		this.clientSocket = clientSocket;
		this.remoteAddress = clientSocket.getRemoteSocketAddress().toString();
		this.httpServerConfig = httpServerConfig;
	}

	@Override
	public void run() {
		try {
			execute();
		} catch (Exception e) {
			LOGGER.error("Client request failed: " + e.getMessage(), e);
		}
	}

	protected void execute() throws Exception {
		try (Socket s = clientSocket) {
			s.setKeepAlive(false);
			try (InputStream in = s.getInputStream(); OutputStream out = s.getOutputStream()) {
				processRequest(remoteAddress, in, out);
			}
		}
	}

	protected void processRequest(String remoteAddress, InputStream in, OutputStream out) throws IOException {
		ReadableHttpResponse response = httpServerConfig.getHttpResponseBuilder().buildNewHttpResponse();
		String startingLine = null;
		try {
			HttpRequest request = httpServerConfig.getHttpRequestParser().parseHttpRequest(in, remoteAddress);
			startingLine = request.getStartingLine();
			processResponse(request, response);
		} catch (AbstractRequestParseFailedExсeption e) {
			startingLine = e.getStartingLine();
			handleException(e, response);
		} catch (EOFException e) {
			LOGGER.warn("Client socket closed connection");
			return;
		}
		httpServerConfig.getHttpResponseBuilder().prepareHttpResponse(response,
				startingLine.startsWith(Constants.HEAD));
		ACCESS_LOGGER.info("Request: {} - \"{}\", Response: {} ({} bytes)", remoteAddress, startingLine,
				response.getStatus(), response.getBodyLength());
		httpServerConfig.getHttpResponseWriter().writeHttpResponse(out, response);
	}

	protected void processResponse(HttpRequest request, HttpResponse response) {
		HttpServerContext context = httpServerConfig.getHttpServerContext();
		try {
			httpServerConfig.getHttpRequestDispatcher().handle(context, request, response);
		} catch (Exception e) {
			handleException(e, response);
		}
	}

	protected void handleException(Exception ex, HttpResponse response) {
		LOGGER.error("Exception during request: " + ex.getMessage(), ex);
		if (ex instanceof HttpServerException) {
			HttpServerException e = (HttpServerException) ex;
			response.setStatus(e.getStatusCode());
			if (e instanceof MethodNotAllowedException) {
				response.setHeaders("Allow", StringUtils.join(Constants.ALLOWED_METHODS, ", "));
			}
		} else {
			response.setStatus(500);
		}
	}

}
