package study.httpserver.io.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import study.httpserver.io.Constants;
import study.httpserver.io.config.HttpResponseWriter;
import study.httpserver.io.config.HttpServerConfig;
import study.httpserver.io.config.ReadableHttpResponse;


class DefaultHttpResponseWriter extends AbstractHttpConfigurableComponent implements HttpResponseWriter {

	DefaultHttpResponseWriter(HttpServerConfig httpServerConfig) {
		super(httpServerConfig);
	}

	@Override
	public void writeHttpResponse(OutputStream out, ReadableHttpResponse response) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)));
		addStartingLine(pw, response);
		addHeaders(pw, response);
		pw.println();
		pw.flush();
		addMassegeBody(out, response);
	}

	private void addStartingLine(PrintWriter pw, ReadableHttpResponse response) {
		String httpVersion = Constants.HTTP_VERSION;
		int status = response.getStatus();
		String statusMassege = httpServerConfig.getStatusMessage(status);
		pw.println(String.format("%s %s %s", httpVersion, status, statusMassege));

	}

	private void addHeaders(PrintWriter pw, ReadableHttpResponse response) {
		Map<String, String> headers = response.getHeaders();
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			pw.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
		}

	}

	private void addMassegeBody(OutputStream out, ReadableHttpResponse response) throws IOException {
		if (!response.isBodyEmpty()) {
			out.write(response.getBody());
			out.flush();
		}
	}

}
