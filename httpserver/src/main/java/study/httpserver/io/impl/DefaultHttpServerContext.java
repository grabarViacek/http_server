package study.httpserver.io.impl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Properties;

import javax.sql.DataSource;

import study.httpserver.io.Constants;
import study.httpserver.io.HtmlTemplateManager;
import study.httpserver.io.HttpServerContext;
import study.httpserver.io.ServerInfo;
import study.httpserver.io.config.HttpServerConfig;
import study.httpserver.io.exception.HttpServerConfigException;

public class DefaultHttpServerContext extends AbstractHttpConfigurableComponent implements HttpServerContext {

	DefaultHttpServerContext(HttpServerConfig httpServerConfig) {
		super(httpServerConfig);
	}

	private DefaultHttpServerConfig getHttpServerConfig() {
		return (DefaultHttpServerConfig) httpServerConfig;
	}

	@Override
	public ServerInfo getServerInfo() {
		return getHttpServerConfig().getServerInfo();
	}

	@Override
	public Collection<String> getSupportedMethods() {
		return Constants.ALLOWED_METHODS;
	}

	@Override
	public Properties getSupportedResponseStatuses() {
		Properties prop = new Properties();
		prop.putAll(getHttpServerConfig().getStatusesProperties());
		return prop;
	}

	@Override
	public DataSource getDataSource() {
		if (getHttpServerConfig().getDataSource() != null) {
			return getHttpServerConfig().getDataSource();
		} else {
			throw new HttpServerConfigException("Datasource is not configured for this context");
		}
	}

	@Override
	public Path getRootPath() {
		return getHttpServerConfig().getRootPath();
	}

	@Override
	public String getContentType(String extension) {
		String mimetypes = getHttpServerConfig().getMimeTypesProperties().getProperty(extension);
		return mimetypes != null ? mimetypes : "text/plain";
	}

	@Override
	public HtmlTemplateManager getHtmlTemplateManager() {
		return getHttpServerConfig().getHtmlTemplateManager();
	}

	@Override
	public Integer getExpiresDaysForResource(String extension) {
		if (getHttpServerConfig().getStaticExpiresExtensions().contains(extension)) {
			return getHttpServerConfig().getStaticExpiresDays();
		} else {
			return null;
		}
	}

}
