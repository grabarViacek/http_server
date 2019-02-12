package study.httpserver.io.config;

import java.io.IOException;

import study.httpserver.io.HttpHandler;
import study.httpserver.io.HttpRequest;
import study.httpserver.io.HttpResponse;
import study.httpserver.io.HttpServerContext;

public interface HttpRequestDispatcher extends HttpHandler {

	void handle(HttpServerContext context, HttpRequest request, HttpResponse response) throws IOException;

}
