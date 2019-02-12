package study.httpserver.io.handler;

import study.httpserver.io.HttpRequest;
import study.httpserver.io.HttpResponse;
import study.httpserver.io.HttpServerContext;
import study.httpserver.io.config.HttpRequestDispatcher;

public class HelloWorldHttpHandler implements HttpRequestDispatcher {

	@Override
	public void handle(HttpServerContext context, HttpRequest request, HttpResponse response) {
		response.setBody("<h1>HelloWorld </h1>");
	}

}
