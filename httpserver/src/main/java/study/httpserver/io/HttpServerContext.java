package study.httpserver.io;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Properties;

import javax.sql.DataSource;

public interface HttpServerContext {

	ServerInfo getServerInfo();

	Collection<String> getSupportedMethods();

	Properties getSupportedResponseStatuses();

	DataSource getDataSource();

	Path getRootPath();

	String getContentType(String extension);

	HtmlTemplateManager getHtmlTemplateManager();

	Integer getExpiresDaysForResource(String extension);
}
