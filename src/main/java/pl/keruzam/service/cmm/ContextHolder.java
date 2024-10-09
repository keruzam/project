package pl.keruzam.service.cmm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Referencja do kontekstu
 * 
 * @author Mirek Szajowski
 * 
 */
public class ContextHolder implements ApplicationContextAware {
	static ApplicationContext context;
	static Map<String, Object> dictionaryBinding = new HashMap<String, Object>();

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static <I> I getBean(final Class<I> serviceClass) {
		return context.getBean(serviceClass);
	}

	public static Object getBean(final String serviceName) {
		return context.getBean(serviceName);
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static Map<String, Object> getDictionaryBinding() {
		return dictionaryBinding;
	}
}
