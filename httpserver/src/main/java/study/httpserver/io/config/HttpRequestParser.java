package study.httpserver.io.config;

import java.io.IOException;
import java.io.InputStream;

import study.httpserver.io.HttpRequest;
import study.httpserver.io.exception.HttpServerException;

public interface HttpRequestParser {

	HttpRequest parseHttpRequest(InputStream inputStream, String remoteAdress) throws IOException, HttpServerException;
}
