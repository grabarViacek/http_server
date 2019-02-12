package study.httpserver.io.config;

import java.net.Socket;
import java.util.concurrent.ThreadFactory;

import study.httpserver.io.HttpServerContext;
import study.httpserver.io.ServerInfo;

public interface HttpServerConfig extends AutoCloseable {

	ServerInfo getServerInfo();

	String getStatusMessage(int status);

	HttpRequestParser getHttpRequestParser();

	HttpResponseBuilder getHttpResponseBuilder();

	HttpResponseWriter getHttpResponseWriter();

	HttpRequestDispatcher getHttpRequestDispatcher();

	HttpServerContext getHttpServerContext();

	ThreadFactory getWorkerThreadFactory();

	HttpClientSocketHandler buildNewHttpClientSocketHandler(Socket clientSocket);

}
