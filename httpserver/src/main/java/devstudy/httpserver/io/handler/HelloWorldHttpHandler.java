package devstudy.httpserver.io.handler;

import devstudy.httpserver.io.HttpRequest;
import devstudy.httpserver.io.HttpResponse;
import devstudy.httpserver.io.HttpServerContext;
import devstudy.httpserver.io.config.HttpRequestDispatcher;

public class HelloWorldHttpHandler implements HttpRequestDispatcher {

	@Override
	public void handle(HttpServerContext context, HttpRequest request, HttpResponse response) {
		response.setBody("<h1>HelloWorld </h1>");
	}

}
