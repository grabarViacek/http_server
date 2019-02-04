package devstudy.httpserver.io.config;

import java.io.IOException;
import java.io.InputStream;

import devstudy.httpserver.io.HttpRequest;
import devstudy.httpserver.io.exeption.HttpServerException;

public interface HttpRequestParser {

	HttpRequest parseHttpRequest(InputStream inputStream, String remoteAdress) throws IOException, HttpServerException;
}
