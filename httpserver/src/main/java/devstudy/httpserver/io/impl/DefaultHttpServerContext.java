package devstudy.httpserver.io.impl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Properties;

import javax.sql.DataSource;

import devstudy.httpserver.io.Constants;
import devstudy.httpserver.io.HtmlTemplateManager;
import devstudy.httpserver.io.HttpServerContext;
import devstudy.httpserver.io.ServerInfo;
import devstudy.httpserver.io.config.HttpServerConfig;
import devstudy.httpserver.io.exeption.HttpServerException;

public class DefaultHttpServerContext extends AbstractHttpConfigurableComponent implements HttpServerContext {

	DefaultHttpServerContext(HttpServerConfig httpServerConfig) {
		super(httpServerConfig);
	}

	private DefaultHttpServerConfig getDefaultHttpServerConfig() {
		return (DefaultHttpServerConfig) httpServerConfig;
	}

	@Override
	public ServerInfo getServerInfo() {
		return getDefaultHttpServerConfig().getServerInfo();
	}

	@Override
	public Collection<String> getSupportedMethods() {
		return Constants.ALLOWED_METHODS;
	}

	@Override
	public Properties getSupportedResponseStatuses() {
		Properties prop = new Properties();
		prop.putAll(getDefaultHttpServerConfig().getStatusesProperties());
		return null;
	}

	@Override
	public DataSource getDataSource() {
		if (getDefaultHttpServerConfig().getDataSource() != null) {
			return getDefaultHttpServerConfig().getDataSource();
		} else {
			throw new HttpServerException("Datasourse is not configured for this context");
		}
	}

	@Override
	public Path getRootPath() {
		return getDefaultHttpServerConfig().getRootPath();
	}

	@Override
	public String getContentType(String extension) {
		String mimetypes = getDefaultHttpServerConfig().getMimeTypesProperties().getProperty(extension);
		return mimetypes != null ? mimetypes : "text/plain";
	}

	@Override
	public HtmlTemplateManager getHtmlTemplateManager() {
		return getDefaultHttpServerConfig().getHtmlTemplateManager();
	}

	@Override
	public Integer getExpiresDaysForResource(String extension) {
		return getDefaultHttpServerConfig().getStaticExpiresDays();
	}

}
