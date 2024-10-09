package pl.keruzam.service.cmm;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Sprawdza poprawnosc serwisow aplikacyjnych
 *
 * @author Mirek Szajowski
 */
public class ApplicationServiceValidator {

	public static void validate(final Class<?> userClass) {
		Method[] declaredMethods = AbstractApplicationService.class.getDeclaredMethods();
		Method[] allDeclaredMethods = userClass.getDeclaredMethods();
		List<String> allPublicMethods = findPublicMethods(allDeclaredMethods);
		for (Method method : declaredMethods) {
			if (allPublicMethods.contains(method.getName() + method.getParameterTypes().length)) {
				throw new IllegalStateException(
						"You can override only protected methods from AbstractApplicationService. You overrided " + method.getName() + " in " + userClass);
			}
		}
	}

	private static List<String> findPublicMethods(final Method[] allDeclaredMethods) {
		List<String> result = new ArrayList<String>();
		for (Method method : allDeclaredMethods) {
			if (Modifier.isPublic(method.getModifiers())) {
				result.add(method.getName() + method.getParameterTypes().length);
			}
		}
		return result;
	}
}
