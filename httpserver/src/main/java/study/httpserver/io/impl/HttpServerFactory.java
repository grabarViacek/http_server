package study.httpserver.io.impl;

import java.util.Properties;

import study.httpserver.io.HandlerConfig;
import study.httpserver.io.HttpServer;
import study.httpserver.io.config.HttpServerConfig;


public class HttpServerFactory {
	protected HttpServerFactory() {
	}

	public static HttpServerFactory create() {
		return new HttpServerFactory();
	}

	public HttpServer createHttpServer(HandlerConfig handlerConfig, Properties overrideServerProperties) {
		HttpServerConfig httpServerConfig = new DefaultHttpServerConfig(handlerConfig, overrideServerProperties);
		return new DefaultHttpServer(httpServerConfig);
	}
}
