package devstudy.httpserver.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;

import devstudy.httpserver.io.HttpRequest;
import devstudy.httpserver.io.HttpResponse;
import devstudy.httpserver.io.HttpServerContext;
import devstudy.httpserver.io.config.HttpRequestDispatcher;

public class DefaultHttpHandler implements HttpRequestDispatcher {

	@Override
	public void handle(HttpServerContext context, HttpRequest request, HttpResponse response) throws IOException {
		String url = request.getUri();
		Path path = Paths.get(context.getRootPath().toString() + url);
		if (Files.exists(path)) {
			if (Files.isDirectory(path)) {
				handleFolderUrl(context, response, path);
			} else {
				handleFileUrl(context, response, path);
			}
		} else {
			response.setStatus(404);
			response.setBody("<h1>Not Found</h1>");
		}
	}

	protected void handleFileUrl(HttpServerContext context, HttpResponse response, Path path) throws IOException {
		setEntityHeaders(context, response, path);
		try (InputStream in = Files.newInputStream(path, StandardOpenOption.READ)) {
			response.setBody(in);
		}
	}

	protected void handleFolderUrl(HttpServerContext context, HttpResponse response, Path path) throws IOException {
		String content = getResponseFromFolder(context, response, path);
		response.setBody(content);
	}

	protected String getResponseFromFolder(HttpServerContext context, HttpResponse response, Path dir)
			throws IOException {
		String root = context.getRootPath().toString();
		StringBuilder htmlBody = new StringBuilder();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
			for (Path path : directoryStream) {
				htmlBody.append("<a href=\"").append(getHref(root, path)).append("\">").append(path.getFileName())
						.append("</a><br>\r\n");
			}
		}
		return htmlBody.toString();
	}

	private String getHref(String root, Path path) {
		return path.toString().replace(root, "");
	}

	protected void setEntityHeaders(HttpServerContext context, HttpResponse response, Path path) throws IOException {
		String extension = FilenameUtils.getExtension(path.toString());
		response.setHeaders("Content-type", context.getContentType(extension));
		response.setHeaders("Last-Modified", Files.getLastModifiedTime(path, LinkOption.NOFOLLOW_LINKS));
		Integer expiresDay = context.getExpriresDaysForResource(extension);
		if (expiresDay != 0) {
			response.setHeaders("Expires", new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(expiresDay)));

		}
	}

}