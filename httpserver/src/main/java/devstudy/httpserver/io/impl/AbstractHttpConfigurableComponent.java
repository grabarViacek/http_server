package devstudy.httpserver.io.impl;

import devstudy.httpserver.io.config.HttpServerConfig;

/**
 * @author Grabar V.A.
 *
 */
public class AbstractHttpConfigurableComponent {
	final HttpServerConfig httpServerConfig;

	AbstractHttpConfigurableComponent(HttpServerConfig httpServerConfig) {
		this.httpServerConfig = httpServerConfig;
	}
}
