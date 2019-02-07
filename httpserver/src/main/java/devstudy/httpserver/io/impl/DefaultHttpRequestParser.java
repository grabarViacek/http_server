package devstudy.httpserver.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import devstudy.httpserver.io.Constants;
import devstudy.httpserver.io.HttpRequest;
import devstudy.httpserver.io.config.HttpRequestParser;
import devstudy.httpserver.io.exeption.BadRequestException;
import devstudy.httpserver.io.exeption.HttpServerException;
import devstudy.httpserver.io.exeption.HttpVersionNotSupportedException;
import devstudy.httpserver.io.exeption.MethodNotAllowedException;
import devstudy.httpserver.io.utils.DataUtils;
import devstudy.httpserver.io.utils.HttpUtils;

class DefaultHttpRequestParser implements HttpRequestParser {

	@Override
	public HttpRequest parseHttpRequest(InputStream inputStream, String remoteAdress)
			throws IOException, HttpServerException {
		String startingLine = null;
		try {
			ParsedRequest parsedRequest = parseInputStrim(inputStream);
			return prepareHttpRequest(parsedRequest, remoteAdress);
		} catch (RuntimeException e) {
			if (e instanceof HttpServerException) {
				throw e;
			} else {
				throw new BadRequestException("Can't parse http request: " + e.getMessage(), e, startingLine);
			}
		}

	}

	protected HttpRequest prepareHttpRequest(ParsedRequest parsedRequest, String remoteAdress) throws IOException {
		String[] startingLine = parsedRequest.startingLine.split(" ");
		String method = startingLine[0];
		String uri = startingLine[1];
		String httpVersion = startingLine[2];
		validateHttpVersion(httpVersion, parsedRequest.startingLine);
		Map<String, String> headers = parseHeaders(parsedRequest.headersLine);
		ProcessedUri processedUri = extractParametersIfPresent(method, uri, httpVersion, parsedRequest.messageBody);
		return new DefaultHttpRequest(remoteAdress, method, processedUri.uri, httpVersion, headers,
				processedUri.parameters);
	}

	protected ProcessedUri extractParametersIfPresent(String method, String uri, String httpVersion, String messageBody)
			throws UnsupportedEncodingException {
		Map<String, String> params = Collections.emptyMap();
		if (Constants.GET.equals(method) || Constants.HEAD.equals(method)) {
			int indexParam = uri.indexOf('?');
			if (indexParam != -1) {
				return extractParametersFromUri(uri, indexParam);
			}
		} else if (Constants.POST.equalsIgnoreCase(method)) {
			if (messageBody != null && !"".equals(messageBody)) {
				params = getParameters(messageBody);
			}
		} else {
			throw new MethodNotAllowedException(method, String.format("%s %s %s", method, uri, httpVersion));
		}
		return new ProcessedUri(uri, params);
	}

	protected Map<String, String> getParameters(String paramString) throws UnsupportedEncodingException {
		Map<String, String> params = new LinkedHashMap<>();
		String[] allParameters = paramString.split("&");
		for (String string : allParameters) {
			String[] splitString = string.split("=");
			if (splitString.length == 1) {
				splitString = new String[] { splitString[0], "" };
			}
			String name = splitString[0];
			String value = params.get(name);
			if (value != null) {
				value += "," + URLDecoder.decode(splitString[1], "UTF-8");
			} else {
				value = URLDecoder.decode(splitString[1], "UTF-8");
			}
			params.put(name, value);
		}
		return params;
	}

	protected ProcessedUri extractParametersFromUri(String uri, int indexParam) throws UnsupportedEncodingException {
		String params = uri.substring(indexParam + 1);
		uri = uri.substring(0, indexParam);
		Map<String, String> map = getParameters(params);
		return new ProcessedUri(uri, map);
	}

	protected void validateHttpVersion(String httpVersion, String startingLine) {
		if (!httpVersion.equals(Constants.HTTP_VERSION)) {
			throw new HttpVersionNotSupportedException("Current server supports only HTTP/1.1 protocol", startingLine);
		}
	}

	protected Map<String, String> parseHeaders(List<String> headers) throws IOException {
		Map<String, String> map = new LinkedHashMap<>();
		String prevName = null;
		for (String string : headers) {
			prevName = putHeader(string, map, prevName);
		}
		return map;
	}

	protected String putHeader(String header, Map<String, String> map, String prevName) {
		if (header.charAt(0) == ' ') {
			String value = map.get(prevName) + header.trim();
			map.put(prevName, value);
			return prevName;
		} else {
			String name = HttpUtils.normilizeHeaderName(header.substring(0, header.indexOf(':')));
			String value = header.substring(header.indexOf(':') + 1).trim();
			map.put(name, value);
			return name;
		}

	}

	protected ParsedRequest parseInputStrim(InputStream inputStream) throws IOException {
		String startingLineAndHeaders = HttpUtils.readStartingLineAndHeaders(inputStream);
		int indexContentLength = HttpUtils.getIndexOfContentBody(startingLineAndHeaders);
		if (indexContentLength != -1) {
			int contentLength = HttpUtils.getContentLenght(startingLineAndHeaders, indexContentLength);
			String messageBody = HttpUtils.readMessageBody(inputStream, contentLength);
			return new ParsedRequest(startingLineAndHeaders, messageBody);
		} else {
			return new ParsedRequest(startingLineAndHeaders, null);
		}
	}

	private static class ParsedRequest {
		private final String startingLine;
		private final List<String> headersLine;
		private final String messageBody;

		public ParsedRequest(String startingLineAndHeaders, String messageBody) {
			super();
			List<String> list = DataUtils.convertToLineList(startingLineAndHeaders);
			this.startingLine = list.remove(0);
			if (list.isEmpty()) {
				this.headersLine = Collections.emptyList();
			} else {
				this.headersLine = Collections.unmodifiableList(list);
			}
			this.messageBody = messageBody;
		}
	}

	private static class ProcessedUri {
		final String uri;
		final Map<String, String> parameters;

		ProcessedUri(String uri, Map<String, String> parameters) {
			super();
			this.uri = uri;
			this.parameters = parameters;
		}
	}
}
