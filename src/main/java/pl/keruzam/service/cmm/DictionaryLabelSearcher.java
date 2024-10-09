package pl.keruzam.service.cmm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import org.springframework.aop.support.AopUtils;
import pl.keruzam.db.DatabaseManager;
import pl.keruzam.service.cmm.context.RequestContext;
import pl.keruzam.service.cmm.lib.Dictionary;
import pl.keruzam.service.cmm.lib.DictionaryBinding;
import pl.keruzam.service.cmm.stereotypes.BusinessService;

/**
 * Szuka dostawcow slownikow i uzupelnia brakujace etykiety
 *
 * @author Mirek Szajowski
 */
@BusinessService
public class DictionaryLabelSearcher {

	@Inject
	RequestContext requestContext;

	public DictionaryLabelSearcher() {
	}

	public DictionaryLabelSearcher(final RequestContext requestContext) {
		this.requestContext = requestContext;
	}

	public void initLabels(final Object object) {
		requestContext.put(DatabaseManager.CACHE_QUERY_RESULTS, Boolean.TRUE);
		try {
			Fields dictionaryFields = findDictionaryFields(object);
			for (Dictionary dictionary : dictionaryFields.dictionaryList) {
				initLabel(dictionary, dictionaryFields.dictionaryName.get(dictionary.toString()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		requestContext.put(DatabaseManager.CACHE_QUERY_RESULTS, Boolean.FALSE);

	}

	public Fields findDictionaryFields(final Object object) throws Exception {
		return findDictionaryFields(new Fields(), object);
	}

	private Fields findDictionaryFields(final Fields result, final Object object) throws Exception {
		List<Field> fields = new ArrayList<Field>();
		fields = getAllFields(fields, object.getClass());
		for (Field field : fields) {
			if (fieldHasValue(object, field)) {
				if (dictionaryType(field)) {
					result.addField(field, object);
				} else if (shouldSearchDeeper(field)) {
					searchDepper(result, object, field);
				}
			}
		}
		return getFieldsFromSuperclass(result, object);
	}

	public List<Field> getAllFields(final List<Field> fields, final Class<?> type) {
		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		if (type.getSuperclass() != null) {
			return getAllFields(fields, type.getSuperclass());
		}
		return fields;
	}

	private Fields getFieldsFromSuperclass(final Fields result, final Object object) throws Exception {
		Field[] declaredFieldsFromSuperclass = object.getClass().getSuperclass().getDeclaredFields();
		for (Field field : declaredFieldsFromSuperclass) {
			if (fieldHasValue(object, field)) {
				if (dictionaryType(field)) {
					result.addField(field, object);
				}
			}
		}
		return result;
	}

	private void searchDepper(final Fields result, final Object object, final Field field) throws Exception {
		Object value = getValue(object, field);
		if (arrayType(field)) {
			for (Object element : (Object[]) value) {
				findDictionaryFields(result, element);
			}
		} else {
			findDictionaryFields(result, value);
		}
	}

	private Object getValue(final Object object, final Field field) {
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean arrayType(final Field field) {
		return field.getType().isArray();
	}

	private boolean dictionaryType(final Field field) {
		return field.getType().equals(Dictionary.class);
	}

	private boolean fieldHasValue(final Object object, final Field field) {
		return getValue(object, field) != null;
	}

	public boolean shouldSearchDeeper(final Field field) {
		Class<?> type = field.getType();
		if (type.isArray() && AbstractDto.class.isAssignableFrom(type.getComponentType())) {
			return true;
		} else if (AbstractDto.class.isAssignableFrom(field.getType())) {
			return true;
		}
		return false;
	}

	public Object getDicnionaryProvider(final Field field) {
		return getDicnionaryProvider(field.getName());
	}

	private Object getDicnionaryProvider(final String fieldName) {
		Object object = ContextHolder.getDictionaryBinding().get(fieldName);
		if (object == null) {
			throw new IllegalStateException("Can't find dictionaryProvider for binding " + fieldName);
		}
		return object;
	}

	public boolean isApplicationServiceProvider(final Object provider) {
		Class<?> targetClass = AopUtils.getTargetClass(provider);
		return BaseApplicationService.class.isAssignableFrom(targetClass);
	}

	public Method getMethodProvider(final Object provider, final String binding) throws Exception {
		Class<?> targetClass = AopUtils.getTargetClass(provider);
		for (Method method : targetClass.getDeclaredMethods()) {
			DictionaryBinding methodAnnotation = AnnotationCache.getMethodAnnotation(method, DictionaryBinding.class);
			if (methodAnnotation != null && containsBinding(methodAnnotation.value(), binding)) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				return provider.getClass().getDeclaredMethod(method.getName(), parameterTypes);
			}
		}
		return null;
	}

	private boolean containsBinding(final String[] value, final String binding) {
		for (String element : value) {
			if (binding.equals(element)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public void initLabel(final Dictionary dictionary, final String fieldName) throws Exception {
		if (dictionary.getLabel() == null) {
			Object provider = getDicnionaryProvider(fieldName);
			AbstractRow row = null;
			Long id = dictionary.getId();
			//System.out.println("Binding " + fieldName + " of id " + id + " and provider: " + getMethodProvider(provider, fieldName));
			if (isApplicationServiceProvider(provider)) {
				try {
					row = ((BaseApplicationService) provider).findById(id);
				} catch (IllegalArgumentException iae) {
					System.out.println(iae.getMessage());
				}
			} else {
				row = findRowInMethodProvider(fieldName, provider, id);
			}
			if (row == null) {
				throw new IllegalStateException(
						"Cant find row for binding " + fieldName + " of id " + id + " and provider: " + getMethodProvider(provider, fieldName));
			}
			dictionary.setLabel(row.getLabel());
		}
	}

	private AbstractRow findRowInMethodProvider(final String fieldName, final Object provider, final Long id) throws Exception {
		Method methodProvider = getMethodProvider(provider, fieldName);
		List<AbstractRow> data = getMethodProviderValue(methodProvider, provider);
		for (AbstractRow element : data) {
			if (id.equals(element.getId())) {
				return element;
			}
		}
		return null;
	}

	/**
	 * jezeli metoda posiada parametr searchText, pobieram cala liste TODO refaktor GlobalDictionaryService - tworzenie sztucznych metod ktore wyciagna pozycje
	 * po id zamiast calej listy
	 */
	@SuppressWarnings("unchecked")
	private List<AbstractRow> getMethodProviderValue(final Method methodProvider, final Object provider) {
		try {
			Class<?>[] parameterTypes = methodProvider.getParameterTypes();
			if (parameterTypes.length == 1) {
				if (parameterTypes[0].getName().equalsIgnoreCase("java.lang.String")) {
					return (List<AbstractRow>) methodProvider.invoke(provider, "");
				}
			}
			return (List<AbstractRow>) methodProvider.invoke(provider);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Przechowuje dane o polach
	 *
	 * @author Mirek Szajowski
	 */
	public static class Fields {
		public List<Dictionary> dictionaryList = new ArrayList<Dictionary>();
		public Map<String, String> dictionaryName = new HashMap<String, String>();

		public void addField(final Field field, final Object object) throws Exception {
			dictionaryList.add((Dictionary) field.get(object));
			dictionaryName.put(field.get(object).toString(), field.getName());

		}
	}

}
