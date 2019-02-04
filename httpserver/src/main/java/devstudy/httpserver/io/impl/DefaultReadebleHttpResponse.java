package devstudy.httpserver.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;

import devstudy.httpserver.io.config.ReadableHttpResponse;
import devstudy.httpserver.io.exeption.HttpServerException;
import devstudy.httpserver.io.utils.HttpUtils;

class DefaultReadebleHttpResponse implements ReadableHttpResponse {
	private int status;
	private Map<String, String> headers;
	private byte[] body;

	protected DefaultReadebleHttpResponse() {
		this.status = 200;
		this.headers = new LinkedHashMap<>();
		this.body = new byte[0];
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public void setHeaders(String name, Object value) {
		Objects.requireNonNull(name, "Name can't be null");
		Objects.requireNonNull(name, "Name can't be null");
		HttpUtils.normilizeHeaderName(name);
		if (value instanceof Date) {
			headers.put(name, new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(value));
		} else if (value instanceof FileTime) {
			headers.put(name, new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
					.format(new Date(((FileTime) value).toMillis())));
		} else {
			headers.put(name, String.valueOf(value));
		}
	}

	@Override
	public void setBody(String content) {
		Objects.requireNonNull(content, "Content can't be null");
		this.body = content.getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public void setBody(InputStream in) {
		try {
			Objects.requireNonNull(in, "InputStream can't be null");
			this.body = IOUtils.toByteArray(in);
		} catch (IOException e) {
			new HttpServerException("Can't set response body from InputStream" + e.getMessage(), e);
		}
	}

	@Override
	public void setBody(Reader reader) {
		try {
			Objects.requireNonNull(reader, "Reader can't be null");
			this.body = IOUtils.toByteArray(reader, StandardCharsets.UTF_8);
		} catch (IOException e) {
			new HttpServerException("Can't set response body from Reader" + e.getMessage(), e);
		}
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public byte[] getBody() {
		return body;
	}

	@Override
	public boolean isBodyEmpty() {
		return getBodyLength() == 0;
	}

	@Override
	public int getBodyLength() {
		return body.length;
	}
}
