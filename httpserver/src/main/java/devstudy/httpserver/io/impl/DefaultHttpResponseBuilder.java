package devstudy.httpserver.io.impl;

import java.util.Date;
import java.util.Map;

import devstudy.httpserver.io.config.HttpResponseBuilder;
import devstudy.httpserver.io.config.HttpServerConfig;
import devstudy.httpserver.io.config.ReadableHttpResponse;
import devstudy.httpserver.io.utils.DataUtils;

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
		if (response.getStatus() >= 400 && response.isBodyEmpty()) {
			setDefaultResponseErrorBody(response);
		}
		setContentLength(response);
		if (clearBody) {
			clearBody(response);
		}
	}

	protected void setDefaultResponseErrorBody(ReadableHttpResponse response) {
		Map<String, Object> args = DataUtils.buildMap(new Object[][] { { "STATUS-CODE", response.getStatus() },
				{ "STATUS-MESSAGE", httpServerConfig.getStatusMessage(response.getStatus()) } });
		String content = httpServerConfig.getHttpServerContext().getHtmlTemplateManager().processTemplate("error.html",
				args);
		response.setBody(content);
	}

	protected void setContentLength(ReadableHttpResponse response) {
		response.setHeaders("Content-Length", response.getBodyLength());
	}

	protected void clearBody(ReadableHttpResponse response) {
		response.setBody("");
	}
}
