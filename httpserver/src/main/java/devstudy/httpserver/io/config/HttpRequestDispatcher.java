package devstudy.httpserver.io.config;

import java.io.IOException;

import devstudy.httpserver.io.HttpHandler;
import devstudy.httpserver.io.HttpRequest;
import devstudy.httpserver.io.HttpResponse;
import devstudy.httpserver.io.HttpServerContext;

public interface HttpRequestDispatcher extends HttpHandler {

	void handle(HttpServerContext context, HttpRequest request, HttpResponse response) throws IOException;

}
