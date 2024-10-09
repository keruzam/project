package pl.keruzam.service.cmm.context;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Kontekst rzadania
 *
 * @author Mirek Szajowski
 */
@Component("requestContext")
public class RequestContextImpl implements RequestContext {
	Map<String, Object> contextData = new HashMap<String, Object>();

	public static RequestContextImpl create() {
		return ContextBeansFactory.createRequestContextProxy();
	}

	@Override
	public Object get(final String key) {
		return contextData.get(key);
	}

	@Override
	public void put(final String key, final Object value) {
		contextData.put(key, value);
	}

	@Override
	public String getCurrentAppLocation() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			return request.getServerName();
		}
		return null;
	}

}
