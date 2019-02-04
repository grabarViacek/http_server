package devstudy.httpserver.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public interface HttpResponse {

	void setStatus(int status);

	void setHeaders(String name, Object value);

	void setBody(String content);

	void setBody(InputStream in) ;

	void setBody(Reader read);

}
