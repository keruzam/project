package pl.keruzam.service.cmm.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.NamedThreadLocal;

/**
 * Zasieg watkowy
 * 
 * @author Mirek Szajowski
 * 
 */
public class ThreadContextScope implements Scope {

	private static final Log logger = LogFactory.getLog(ThreadContextScope.class);

	private static ThreadLocal<Map<String, Object>> contextData = new NamedThreadLocal<Map<String, Object>>("NextThreadContextData") {

		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}

	};

	@Override
	public Object get(final String name, final ObjectFactory objectFactory) {
		Map<String, Object> scope = contextData.get();
		Object object = scope.get(name);
		if (object == null) {
			object = objectFactory.getObject();
			scope.put(name, object);
		}
		return object;
	}

	@Override
	public String getConversationId() {
		return Thread.currentThread().getName();
	}

	// TODO zamplementuj bo np uzycie cache o zasiegu requestu,uzytkownika bedzie cieklo
	@Override
	public void registerDestructionCallback(final String name, final Runnable callback) {
		// logger.warn("SimpleThreadScope does not support descruction callbacks. " +
		// "Consider using a RequestScope in a Web environment.");
	}

	@Override
	public Object remove(final String name) {
		Map<String, Object> scope = contextData.get();
		return scope.remove(name);
	}

	@Override
	public Object resolveContextualObject(final String key) {
		return null;
	}

	public static void clearContext() {
		contextData.remove();
	}

}
