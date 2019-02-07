package devstudy.httpserver.io.handler;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import devstudy.httpserver.io.Constants;
import devstudy.httpserver.io.HttpHandler;
import devstudy.httpserver.io.HttpRequest;
import devstudy.httpserver.io.HttpResponse;
import devstudy.httpserver.io.HttpServerContext;
import devstudy.httpserver.io.utils.DataUtils;


public class ServerInfoHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpServerContext context, HttpRequest request, HttpResponse response) throws IOException {
		if (Constants.GET.equals(request.getMethod())) {
			Map<String, Object> args = getDataMap(context);
			response.setBody(context.getHtmlTemplateManager().processTemplate("server-info.html", args));
		} else {
			response.setStatus(400);
		}
	}

	protected Map<String, Object> getDataMap(HttpServerContext context) {
		int threadCount = context.getServerInfo().getThreadCount();
		return DataUtils.buildMap(new Object[][] { { "SERVER-NAME", context.getServerInfo().getName() },
				{ "SERVER-PORT", context.getServerInfo().getPort() },
				{ "THREAD-COUNT", threadCount == 0 ? "UNLIMITED" : threadCount },
				{ "SUPPORTED-REQUEST-METHODS", context.getSupportedMethods() },
				{ "SUPPORTED-RESPONSE-STATUSES", getSupportedResponseStatuses(context) } });
	}

	protected StringBuilder getSupportedResponseStatuses(HttpServerContext context) {
		StringBuilder s = new StringBuilder();
		Map<Object, Object> map = new TreeMap<>(context.getSupportedResponseStatuses());
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			s.append(entry.getKey()).append(" [").append(entry.getValue()).append("]<br>");
		}
		return s;
	}
}
