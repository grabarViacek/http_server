package devstudy.httpserver.io.config;

import java.net.Socket;
import java.util.concurrent.ThreadFactory;

import devstudy.httpserver.io.HttpServerContext;
import devstudy.httpserver.io.ServerInfo;

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
