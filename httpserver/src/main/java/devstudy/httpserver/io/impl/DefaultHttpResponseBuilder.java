package devstudy.httpserver.io.impl;

import java.util.Date;

import devstudy.httpserver.io.config.HttpResponseBuilder;
import devstudy.httpserver.io.config.HttpServerConfig;
import devstudy.httpserver.io.config.ReadableHttpResponse;

class DefaultHttpResponseBuilder extends AbstractHttpConfigurableComponent implements HttpResponseBuilder {

	DefaultHttpResponseBuilder(HttpServerConfig httpServerConfig) {
		super(httpServerConfig);
	}

	private DefaultReadebleHttpResponse createNewReadebleHttpResponseInstanse() {
		return new DefaultReadebleHttpResponse();
	}

	@Override
	public ReadableHttpResponse buildNewHttpResponse() {
		ReadableHttpResponse response = createNewReadebleHttpResponseInstanse();
		response.setHeaders("Date", new Date());
		response.setHeaders("Server", httpServerConfig.getServerInfo().getName());
		response.setHeaders("Content-Language", "en");
		response.setHeaders("Conection", "close");
		response.setHeaders("Content-Type", "text/html");
		return response;
	}

	@Override
	public void prepareHttpResponse(ReadableHttpResponse response, boolean clearBody) {
		response.setHeaders("Content-length", response.getBodyLength());
		if (clearBody) {
			clearBody(response);
		}
	}

	private void clearBody(ReadableHttpResponse response) {
		response.setBody("");
	}
}
