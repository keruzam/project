package pl.keruzam.service.cmm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Klasa odpowiedzialna za cache'owanie annotacje
 *
 * @author Mirek Szajowski
 * @author Roman Heppel
 */
public class AnnotationCache {

	private static Map<String, Map<String, Map<String, Annotation>>> methodAnnotation = new ConcurrentHashMap<String, Map<String, Map<String, Annotation>>>();

	private static Map<String, Map<String, Annotation>> classAnnotation = new ConcurrentHashMap<String, Map<String, Annotation>>();

	private static Map<String, Map<String, Map<String, Annotation>>> fieldAnnotation = new ConcurrentHashMap<String, Map<String, Map<String, Annotation>>>();

	/**
	 * Zwraca annotacje do pola
	 *
	 * @param <T>
	 * 		annotacja
	 * @param field
	 * 		pole
	 * @param annotationClass
	 * 		klasa poszukiwanej anotacji
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T getFieldAnnotation(final Field field, final Class<? extends T> annotationClass) {

		String className = field.getDeclaringClass().getName();
		Map<String, Map<String, Annotation>> fieldMap = fieldAnnotation.get(className);
		if (fieldMap == null) {
			fieldMap = new ConcurrentHashMap<String, Map<String, Annotation>>();
			fieldAnnotation.put(className, fieldMap);
		}

		String fieldName = field.getName();
		Map<String, Annotation> annotationMap = fieldMap.get(fieldName);
		if (annotationMap == null) {
			annotationMap = new ConcurrentHashMap<String, Annotation>();
			fieldMap.put(fieldName, annotationMap);
		}

		String annotationName = annotationClass.getCanonicalName();
		T annotation = (T) annotationMap.get(annotationName);

		if (annotation == null) {
			annotation = field.getAnnotation(annotationClass);
			if (annotation == null) {
				annotation = (T) NullAnnotation.getInstance();
			}
			annotationMap.put(annotationName, annotation);
		}
		if (annotation != NullAnnotation.getInstance()) {
			return annotation;
		}
		return null;
	}

	/**
	 * Zwraca annotacje do metody
	 *
	 * @param <T>
	 * 		annotacja
	 * @param method
	 * 		metoda
	 * @param annotationClass
	 * 		klasa poszukiwanej anotacji
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T getMethodAnnotation(final Method method, final Class<? extends T> annotationClass) {
		String className = method.getDeclaringClass().getName();
		Map<String, Map<String, Annotation>> fieldMap = methodAnnotation.get(className);
		if (fieldMap == null) {
			fieldMap = new ConcurrentHashMap<String, Map<String, Annotation>>();
			methodAnnotation.put(className, fieldMap);
		}

		String fieldName = method.getName();
		Map<String, Annotation> annotationMap = fieldMap.get(fieldName);
		if (annotationMap == null) {
			annotationMap = new ConcurrentHashMap<String, Annotation>();
			fieldMap.put(fieldName, annotationMap);
		}

		String annotationName = annotationClass.getCanonicalName();
		T annotation = (T) annotationMap.get(annotationName);

		if (annotation == null) {
			annotation = method.getAnnotation(annotationClass);
			if (annotation == null) {
				annotation = (T) NullAnnotation.getInstance();
			}
			annotationMap.put(annotationName, annotation);
		}
		if (annotation != NullAnnotation.getInstance()) {
			return annotation;
		}
		return null;
	}

	/**
	 * Zwraca annotacje do klasy
	 *
	 * @param <T>
	 * 		annotacja
	 * @param clazz
	 * 		klasa
	 * @param annotationClass
	 * 		klasa poszukiwanej anotacji
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T getClassAnnotation(final Class<?> clazz, final Class<? extends T> annotationClass) {

		String className = clazz.getCanonicalName();
		Map<String, Annotation> annotationMap = classAnnotation.get(className);
		if (annotationMap == null) {
			annotationMap = new ConcurrentHashMap<String, Annotation>();
			classAnnotation.put(className, annotationMap);
		}

		String annotationName = annotationClass.getCanonicalName();
		T annotation = (T) annotationMap.get(annotationName);

		if (annotation == null) {
			annotation = clazz.getAnnotation(annotationClass);
			if (annotation == null) {
				annotation = (T) NullAnnotation.getInstance();
			}
			annotationMap.put(annotationName, annotation);
		}
		if (annotation != NullAnnotation.getInstance()) {
			return annotation;
		}
		return null;
	}

	public static void clear() {
		methodAnnotation.clear();
		classAnnotation.clear();
		fieldAnnotation.clear();

	}

	static class NullAnnotation implements Annotation {

		public static NullAnnotation getInstance() {
			return InstanceHolder.INSTANCE;
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return NullAnnotation.class;
		}

		private static class InstanceHolder {

			private static final NullAnnotation INSTANCE = new NullAnnotation();

		}

	}

}
