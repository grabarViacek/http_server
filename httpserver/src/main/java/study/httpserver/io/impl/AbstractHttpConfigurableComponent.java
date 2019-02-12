package study.httpserver.io.impl;

import study.httpserver.io.config.HttpServerConfig;

public class AbstractHttpConfigurableComponent {
	final HttpServerConfig httpServerConfig;

	AbstractHttpConfigurableComponent(HttpServerConfig httpServerConfig) {
		this.httpServerConfig = httpServerConfig;
	}
}
