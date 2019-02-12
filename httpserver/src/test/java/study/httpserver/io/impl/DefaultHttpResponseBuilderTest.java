package study.httpserver.io.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import study.httpserver.io.HtmlTemplateManager;
import study.httpserver.io.HttpServerContext;
import study.httpserver.io.ServerInfo;
import study.httpserver.io.config.HttpResponseBuilder;
import study.httpserver.io.config.HttpServerConfig;
import study.httpserver.io.config.ReadableHttpResponse;
import study.httpserver.io.impl.DefaultHttpResponseBuilder;
import study.httpserver.io.utils.DataUtils;

public class DefaultHttpResponseBuilderTest {
	private HttpResponseBuilder httpResponseBuilder;
	private HttpServerConfig httpServerConfig;

	@Before
	public void before() {
		httpServerConfig = mock(HttpServerConfig.class);
		httpResponseBuilder = new DefaultHttpResponseBuilder(httpServerConfig);
		ServerInfo serverInfo = mock(ServerInfo.class);
		when(httpServerConfig.getServerInfo()).thenReturn(serverInfo);
		when(serverInfo.getName()).thenReturn("study");
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testbuildNewHttpResponse() {
		ReadableHttpResponse response = httpResponseBuilder.buildNewHttpResponse();

		assertEquals("study", response.getHeaders().get("Server"));
		assertEquals("en", response.getHeaders().get("Content-Language"));
		assertEquals("close", response.getHeaders().get("Conection"));
		assertEquals("text/html", response.getHeaders().get("Content-Type"));
		assertNotNull(response.getHeaders().get("Date"));
	}

	@Test
	public void testPrepare_StatusIsOK_BodyIsFound_ClearIsFalse() {
		String content = "Content";
		ReadableHttpResponse response = httpResponseBuilder.buildNewHttpResponse();
		response.setBody(content);
		httpResponseBuilder.prepareHttpResponse(response, false);

		assertNotNull(response.getHeaders().get("Content-Length"));
		assertEquals(String.valueOf(content.length()), response.getHeaders().get("Content-Length"));
		assertArrayEquals(content.getBytes(StandardCharsets.UTF_8), response.getBody());

	}

	@Test
	public void testPrepare_StatusIsOK_BodyIsFound_ClearIsTrue() {
		String content = "Content";
		ReadableHttpResponse response = httpResponseBuilder.buildNewHttpResponse();
		response.setBody(content);
		httpResponseBuilder.prepareHttpResponse(response, true);

		assertNotNull(response.getHeaders().get("Content-Length"));
		assertEquals("7", response.getHeaders().get("Content-Length"));
		assertTrue(response.isBodyEmpty());
	}

	@Test
	public void testPrepare_StatusIs404_BodyIsFound_ClearIsFalse() {
		String content = "Content";
		ReadableHttpResponse response = httpResponseBuilder.buildNewHttpResponse();
		response.setStatus(404);
		response.setBody(content);
		httpResponseBuilder.prepareHttpResponse(response, false);

		assertNotNull(response.getHeaders().get("Content-Length"));
		assertEquals("7", response.getHeaders().get("Content-Length"));
		assertArrayEquals(content.getBytes(StandardCharsets.UTF_8), response.getBody());
	}

	@Test
	public void testPrepare_StatusIs404_BodyIsNotFound_ClearIsFalse() {
		String content = "error.html";
		when(httpServerConfig.getStatusMessage(404)).thenReturn("Not Found");
		HttpServerContext context = mock(HttpServerContext.class);
		when(httpServerConfig.getHttpServerContext()).thenReturn(context);
		HtmlTemplateManager htmlTemplateManager = mock(HtmlTemplateManager.class);
		when(context.getHtmlTemplateManager()).thenReturn(htmlTemplateManager);
		Map<String, Object> args = DataUtils
				.buildMap(new Object[][] { { "STATUS-CODE", 404 }, { "STATUS-MESSAGE", "Not Found" } });
		when(htmlTemplateManager.processTemplate("error.html", args)).thenReturn(content);
		ReadableHttpResponse response = httpResponseBuilder.buildNewHttpResponse();
		response.setStatus(404);

		httpResponseBuilder.prepareHttpResponse(response, false);

		assertNotNull(response.getHeaders().get("Content-Length"));
		assertEquals("10", response.getHeaders().get("Content-Length"));
		assertArrayEquals(content.getBytes(StandardCharsets.UTF_8), response.getBody());
	}

}
