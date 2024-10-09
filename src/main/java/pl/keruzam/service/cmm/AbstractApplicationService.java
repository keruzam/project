package pl.keruzam.service.cmm;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import pl.keruzam.db.BaseRepository;
import pl.keruzam.db.FindCriteria;
import pl.keruzam.service.ResourceService;
import pl.keruzam.service.cmm.context.OperatorContext;
import pl.keruzam.service.cmm.context.RequestContext;
import pl.keruzam.service.cmm.lib.Dictionary;
import pl.keruzam.service.cmm.lib.SortOrder;
import pl.keruzam.service.cmm.stereotypes.ReadOnly;

/**
 * Abstrakcyjny serwis kliencki Pokrywamy metoda "find(final FindCriteria criteria)" jesli chcemy aby nasze serwis obslugiwal wyszukiwanie oraz
 * "findRowsCount(final FindCriteria filterCriteria)" jesli nasza dostawca obsluguje stronnicowanie danych
 *
 * @param <D>
 * @param <R>
 * @author Mirek Szajowski.
 */
public abstract class AbstractApplicationService<D extends AbstractDto, R extends AbstractRow> implements BaseApplicationService<D, R> {

	protected static final Logger logger = LoggerFactory.getLogger(Loggers.DIAGNOSTIC);
	private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	@Inject
	public OperatorContext operatorContext;
	@Inject
	public ResourceService resourceService;
	@Inject
	protected BaseRepository repo;
	@Inject
	protected RequestContext requestContext;
	private Class rowClass;
	private Class dtoClass;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initialize() {
		ApplicationServiceValidator.validate(this.getClass());
		try {
			Class<? extends AbstractApplicationService> genericSource = findGenericSourceClass(getClass());
			dtoClass = (Class) ((ParameterizedType) genericSource.getGenericSuperclass()).getActualTypeArguments()[0];
			rowClass = (Class) ((ParameterizedType) genericSource.getGenericSuperclass()).getActualTypeArguments()[1];
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Szuka klasy abstraktu.
	 */
	private Class<? extends AbstractApplicationService> findGenericSourceClass(final Class<? extends AbstractApplicationService> clazz) {
		Class<? extends AbstractApplicationService> superclass = (Class<? extends AbstractApplicationService>) clazz.getSuperclass();
		if (superclass.equals(AbstractApplicationService.class)) {
			return clazz;
		}
		return findGenericSourceClass(superclass);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public D init() {
		try {
			return init((D) dtoClass.newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public D init(final D dto) {
		initImpl(dto);
		initDictionaryLabels(dto);
		return dto;
	}

	public void initDictionaryLabels(final D dto) {
		new DictionaryLabelSearcher(requestContext).initLabels(dto);
	}

	protected void initImpl(final D dto) {

	}

	/**
	 * Metoda wczytujaca dto po id
	 */
	@ReadOnly
	@Override
	public D load(final Long id) {
		D dto = loadImpl(id);
		new DictionaryLabelSearcher(requestContext).initLabels(dto);
		return dto;
	}

	protected abstract D loadImpl(final Long id);

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Long save(final D dto) {
		// checkAccountLimits();
		Long id = dto.getId();
		if (id != null) {
			update(dto);
			return id;
		}
		beforeValidateImpl(dto);
		validate(dto);
		validateImpl(dto);
		return saveImpl(dto);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Long saveWithoutValidate(final D dto) {
		Long id = dto.getId();
		if (id != null) {
			updateWithoutValidate(dto);
			return id;
		}
		return saveImpl(dto);
	}

	protected void validate(final D dto) {
		Validator validator = factory.getValidator();
		// when
		Set<ConstraintViolation<D>> validate;
		if (dto.getId() != null) {
			validate = validator.validate(dto);
		} else {
			Class[] groups = { Default.class, InsertValidationGroup.class };
			validate = validator.validate(dto, groups);
		}
		if (!validate.isEmpty()) {
			ConstraintViolation<D> constraintViolation = validate.iterator().next();
			BeanValidationError beanValidationError = new BeanValidationError(constraintViolation.getMessage());
			beanValidationError.setValidations(validate.iterator());
			throw beanValidationError;
		}
	}

	protected abstract Long saveImpl(final D dto);

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(final D dto) {
		beforeValidateImpl(dto);
		validate(dto);
		validateImpl(dto);
		updateImpl(dto);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateWithoutValidate(final D dto) {
		updateImpl(dto);
	}

	/**
	 * Domyslna metoda validacyjna uruchamiana przed save oraz update
	 */
	protected void validateImpl(final D dto) {

	}

	protected abstract void updateImpl(final D dto);

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(final Long id) {
		deleteImpl(id);
	}

	protected abstract void deleteImpl(final Long id);

	@ReadOnly
	@Override
	public int findRowsCount(final Filter... filters) {
		return findRowsCount(new FindCriteria(0, FindCriteria.UNLIMITED, null, filters));
	}

	@ReadOnly
	@Override
	public int findRowsCount(final String searchText, final Filter... filters) {
		return findRowsCount(new FindCriteria(0, FindCriteria.UNLIMITED, searchText, filters));
	}

	@ReadOnly
	@Override
	public List<R> findAll(final Filter... filters) {
		return find(new FindCriteria(FindCriteria.UNLIMITED, filters));

	}

	@ReadOnly
	@Override
	public List<R> findAll(final String sortField, final SortOrder sortOrder, final Filter... filters) {
		return find(new FindCriteria(sortField, sortOrder, filters));
	}

	@ReadOnly
	@Override
	public List<R> find(final String searchText, final Filter... filters) {
		return find(new FindCriteria(searchText, filters));
	}

	@ReadOnly
	@Override
	public List<R> find(final String sortField, final SortOrder sortOrder, final String searchText, final Filter... filters) {
		return find(new FindCriteria(searchText, sortField, sortOrder, filters));
	}

	@ReadOnly
	@Override
	public List<R> find(final int first, final int pageSize, final Filter... filters) {
		return find(new FindCriteria(first, pageSize, filters));
	}

	@ReadOnly
	@Override
	public List<R> find(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final String searchText,
			final Filter... filters) {
		return find(new FindCriteria(first, pageSize, sortField, sortOrder, searchText, filters));
	}

	@ReadOnly
	@Override
	public List<R> find(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final Filter... filters) {
		return find(new FindCriteria(first, pageSize, sortField, sortOrder, filters));
	}

	@ReadOnly
	@Override
	public R findById(final Long id) {
		List<R> result = find(new FindCriteria(1, new Filter(AbstractRow.ID_NAME, new Dictionary(id))));
		if (result.size() == 1) {
			return result.get(0);
		}
		return findInList(result, id);
	}

	private R findInList(final List<R> result, final Long id) {
		for (R element : result) {
			if (id.equals(element.getId())) {
				return element;
			}
		}
		throw new IllegalStateException("Cannot find element with id " + id + " in finder for " + this.getClass());
	}

	protected List<R> find(final FindCriteria criteria) {
		return null;
	}

	protected int findRowsCount(final FindCriteria filterCriteria) {
		return 0;
	}

	protected void beforeValidateImpl(final D dto) {
	}
}
