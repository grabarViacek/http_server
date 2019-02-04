package devstudy.httpserver.io.impl;

import java.util.Properties;

import devstudy.httpserver.io.HttpServer;
import devstudy.httpserver.io.config.HttpServerConfig;

public class HttpServerFactory {
	protected HttpServerFactory() {
	}

	public static HttpServerFactory create() {
		return new HttpServerFactory();
	}

	public HttpServer createHttpServer(Properties overrideServerProperties) {
		HttpServerConfig httpServerConfig = new DefaultHttpServerConfig(overrideServerProperties);
		return new DefaultHttpServer(httpServerConfig);
	}
}
