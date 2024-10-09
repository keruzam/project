package pl.keruzam.db;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Filter;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.keruzam.service.cmm.AbstractAccountEntity;
import pl.keruzam.service.cmm.AbstractCompanyEntity;
import pl.keruzam.service.cmm.AbstractOwnerEntity;
import pl.keruzam.service.cmm.Loggers;
import pl.keruzam.service.cmm.context.OperatorContext;
import pl.keruzam.service.cmm.context.RequestContext;
import pl.keruzam.service.cmm.lib.Dictionary;
import pl.keruzam.service.cmm.lib.SortOrder;
import pl.keruzam.service.cmm.stereotypes.BusinessService;

/**
 * Obiekt dostepowy do bazy danych
 *
 * @author Mirek Szajowski
 */
@SuppressWarnings("unchecked")
@BusinessService
public class DatabaseManager {
	public static final String CACHE_QUERY_RESULTS = "cacheQueryResults";

	private static final Logger logger = LoggerFactory.getLogger(Loggers.DIAGNOSTIC);
	@Inject
	OperatorContext operatorContext;
	@Inject
	RequestContext requestContext;

	@Inject
	private SessionFactory sessionFactory;

	public Session getSession() {
		Session session = sessionFactory.getCurrentSession();
		Filter filter = session.enableFilter(AbstractCompanyEntity.COMPANY_FILTER);
		filter.setParameter(AbstractCompanyEntity.ID_COMPANY_FIELD, -1L);

		// FIXME Do czasu poprawy testow, nie mozna dodac filtrowania :-(
		// Filter accountFilter = session.enableFilter(AbstractAccountEntity.ACCOUNT_FILTER);
		// accountFilter.setParameter(AbstractAccountEntity.ID_ACCOUNT_FIELD,
		// operatorContext.getIdAccount() != null ? operatorContext.getIdAccount() : -1);

		Filter ownerFilter = session.enableFilter(AbstractOwnerEntity.OWNER_FILTER);
		ownerFilter.setParameter(AbstractOwnerEntity.ID_OWNER_FIELD, -1L);
		ownerFilter.setParameter(AbstractOwnerEntity.ID_ACCOUNT_FIELD, -1L);
		return session;
	}

	/**
	 * Zwraca nie zabezpieczona sesje. Uwaga duze ryzyko beppieczenstwa
	 */
	public Session getUnprotectedSession() {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.disableFilter(AbstractCompanyEntity.COMPANY_FILTER);
		currentSession.disableFilter(AbstractAccountEntity.ACCOUNT_FILTER);
		currentSession.disableFilter(AbstractOwnerEntity.OWNER_FILTER);
		return currentSession;
	}

	public <E> E load(final Class<E> entityClass, final Long id) {
		Object load = getSession().load(entityClass, id);
		return (E) load;
	}

	public <E> E loadbyNaturalId(final Class<E> entityClass, final Map<String, Serializable> params) {
		NaturalIdLoadAccess byNaturalId = getSession().byNaturalId(entityClass);
		for (Entry<String, Serializable> param : params.entrySet()) {
			byNaturalId.using(param.getKey(), param.getValue());
		}
		return (E) byNaturalId.load();
	}

	public <E> E loadAndLock(final Class<E> entityClass, final Long id) {
		Object load = getSession().load(entityClass, id, LockOptions.UPGRADE);
		return (E) load;
	}

	public <E> E get(final Class<E> entityClass, final Long id) {
		Object load = getSession().get(entityClass, id);
		return (E) load;
	}

	/**
	 * Zaklada blokade pesymistyczna select for update
	 */
	public void lock(final Class<?> entityClass, final Long id) {
		logger.info("Wait for lock " + entityClass.getSimpleName() + " " + id);
		Object load = getSession().load(entityClass, id);
		getSession().buildLockRequest(LockOptions.UPGRADE).setLockMode(LockMode.PESSIMISTIC_WRITE).lock(load);
		logger.info("Lock aquired " + entityClass.getSimpleName() + " " + id);
	}

	/**
	 * Zaklada blokade pesymistyczna select for update
	 */
	public void lock(final Object entity) {
		getSession().buildLockRequest(LockOptions.UPGRADE).setLockMode(LockMode.PESSIMISTIC_WRITE).lock(entity);
	}

	public void delete(final Object entity, final Object... entities) {
		getSession().delete(entity);
		if (entities != null && entities.length > 0) {
			for (Object en : entities) {
				delete(en);
			}
		}
	}

	/**
	 * Tworzy obiekt query na podstawie kryteriow
	 */
	public Query createQuery(final String queryString, final FindCriteria criteria, final String mainAlias) {
		StringBuilder queryCommand = new StringBuilder(queryString);
		SortOrder sortOrder = criteria.getSortOrder();
		String sortField = criteria.getSortField();
		String groupField = criteria.getGroupField();
		Boolean sortFieldWithoutAlias = criteria.isSortFieldWithoutAlias();
		// filtrowanie
		addFilters(criteria, queryCommand, mainAlias);
		// group by
		addGroup(queryCommand, groupField);
		// order by
		addOdrer(queryCommand, mainAlias, sortOrder, sortField, sortFieldWithoutAlias);
		Query query = getSession().createQuery(queryCommand.toString());
		Integer firstRowIndex = criteria.getFirst();
		if (firstRowIndex != null) {
			query.setFirstResult(firstRowIndex);
		}
		Integer pageSize = criteria.getPageSize();
		if (pageSize != null && !criteria.isUnlimited()) {
			query.setMaxResults(pageSize);
		}
		setFilters(criteria, query);
		return query;
	}

	private void addGroup(final StringBuilder queryCommand, final String groupField) {
		if (groupField != null) {
			queryCommand.append(" GROUP BY ");
			queryCommand.append(groupField);
		}
	}

	private void setFilters(final FindCriteria criteria, final Query query) {
		if (!criteria.getFilters().isEmpty() && criteria.containsAutoFilter()) {
			for (pl.keruzam.service.cmm.Filter filter : criteria.getFilters()) {
				if (!criteria.isManualFilter(filter.getName())) {
					if (filter.getValue() instanceof Dictionary) {
						query.setParameter(createQueryParam(filter), ((Dictionary) filter.getValue()).getId());
					} else if (filter.getValue() instanceof Long[] || filter.getValue() instanceof Integer[]) {
						setParameters(query, filter);
					} else {
						query.setParameter(createQueryParam(filter), filter.getValue());
					}
				}
			}
		}
	}

	private void setParameters(final Query query, final pl.keruzam.service.cmm.Filter filter) {
		Object[] array = (Object[]) filter.getValue();
		if (array.length > 1) {
			for (int i = 0; i < array.length; i++) {
				query.setParameter(createQueryParam(filter) + i, array[i]);
			}
		} else {
			query.setParameter(createQueryParam(filter), array[0]);
		}
	}

	private String createQueryParam(final pl.keruzam.service.cmm.Filter filter) {
		return filter.getName().replace(".", "_");
	}

	private void addFilters(final FindCriteria criteria, final StringBuilder queryCommand, final String mailAlias) {
		if (!criteria.getFilters().isEmpty() && criteria.containsAutoFilter()) {
			boolean whereAdded = false;
			if (!containsWhere(queryCommand)) {
				queryCommand.append(" WHERE ");
				whereAdded = true;
			}
			for (pl.keruzam.service.cmm.Filter filter : criteria.getFilters()) {
				String name = filter.getName();
				if (!criteria.isManualFilter(name)) {
					if (!whereAdded) {
						queryCommand.append(" AND ");
					}
					if (!containgAliasSeparator(name)) {
						queryCommand.append(mailAlias);
					}
					if (filter.getValue() instanceof Long[] || filter.getValue() instanceof Integer[]) {
						appendFiltersToQuery(queryCommand, filter, name);
					} else {
						queryCommand.append(name).append(" " + filter.getOperator().getValue() + " :").append(createQueryParam(filter));
					}
					whereAdded = false;
				}
			}
		}
	}

	private void appendFiltersToQuery(final StringBuilder queryCommand, final pl.keruzam.service.cmm.Filter filter, final String name) {
		Object[] array = (Object[]) filter.getValue();
		String logicalOperator = filter.getQueryValueArrayLogicalOperator().toString();
		String alias = "";
		if (array.length > 1) {
			for (int i = 0; i < array.length; i++) {
				if (isLastElement(array, i)) {
					queryCommand.append(" ").append(logicalOperator).append(" ").append(alias).append(name).append(" ").append(filter.getOperator().getValue())
							.append(" :").append(createQueryParam(filter)).append(i).append(")");
				} else if (isNotFirst(i)) {
					queryCommand.append(" ").append(logicalOperator).append(" ").append(alias).append(name).append(" ").append(filter.getOperator().getValue())
							.append(" :").append(createQueryParam(filter)).append(i);
				} else {
					alias = getCurrentAliasFromQuery(queryCommand, alias);
					queryCommand.append("(").append(alias).append(name).append(" ").append(filter.getOperator().getValue()).append(" :")
							.append(createQueryParam(filter)).append(i).append(" ");
				}
			}
		} else {
			queryCommand.append(name).append(" ").append(filter.getOperator().getValue()).append(" :").append(createQueryParam(filter));
		}
	}

	private String getCurrentAliasFromQuery(final StringBuilder queryCommand, final String alias) {
		int length = queryCommand.length();
		String text = queryCommand.substring(length - 1, length);
		if (text.equals(".")) {
			for (int j = length; j > 0; j--) {
				String a = queryCommand.substring(j - 1);
				if (a.contains(" ")) {
					queryCommand.setLength(j);
					return a.trim();
				}
			}
		}
		return alias;
	}

	private boolean isNotFirst(final int i) {
		return i > 0;
	}

	private boolean isLastElement(final Object[] array, final int i) {
		return i + 1 == array.length;
	}

	private boolean containsWhere(final StringBuilder queryCommand) {
		return queryCommand.toString().toUpperCase().contains("WHERE");
	}

	private void addOdrer(final StringBuilder queryCommand, final String mainAlias, final SortOrder sortOrder, final String sortField,
			final Boolean sortFieldWithoutAlias) {
		if (sortField != null && !sortField.equals("1") && sortOrder != SortOrder.UNSORTED) {
			queryCommand.append(" ORDER BY ");
			if (!sortFieldWithoutAlias && !containgAliasSeparator(sortField)) {
				queryCommand.append(mainAlias);
			}
			queryCommand.append(sortField.replaceAll(",", " " + sortOrder.name() + ",")).append(" ").append(sortOrder.name());
		}
	}

	private boolean containgAliasSeparator(final String name) {
		return name.contains(".");
	}

	public CriteriaQuery<Class<?>> createCriteria(final Class<?> clazz) {
		return (CriteriaQuery<Class<?>>) createCriteriaBuilder().createQuery(clazz);
	}

	public HibernateCriteriaBuilder createCriteriaBuilder() {
		return getSession().getCriteriaBuilder();
	}

	public Query getNamedQuery(final String namedQuery) {
		Query query = getSession().getNamedQuery(namedQuery);
		query.setCacheable(Boolean.TRUE.equals(requestContext.get(CACHE_QUERY_RESULTS)));
		return query;
	}

	public void persist(final Object entity, final Object... entities) {
		getSession().persist(entity);
		if (entities != null && entities.length > 0) {
			for (Object en : entities) {
				persist(en);
			}
		}
	}

	public Query createQuery(final String queryString) {
		Query query = getSession().createQuery(queryString);
		query.setCacheable(Boolean.TRUE.equals(requestContext.get(CACHE_QUERY_RESULTS)));
		return query;
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}

	public NativeQuery createQuerySql(final String query) {
		return getSession().createNativeQuery(query);
	}

	public MutationQuery createMutationQuerySql(final String query) {
		return getSession().createNativeMutationQuery(query);
	}

	public void refresh(final Object entity) {
		getSession().refresh(entity);
	}

	public Query getUnprotectedNamedQuery(final String name) {
		return getUnprotectedSession().getNamedQuery(name);
	}

	public Query createUnprotectedQuery(final String queryString) {
		Query query = getUnprotectedSession().createQuery(queryString);
		query.setCacheable(Boolean.TRUE.equals(requestContext.get(CACHE_QUERY_RESULTS)));
		return query;
	}

	public void saveAndFlush(final Object entity) {
		Session session = getSession();
		session.persist(entity);
		session.flush();
	}

	public void saveAndClear(final Object entity) {
		Session session = getSession();
		session.persist(entity);
		session.flush();
		session.clear();
	}

	public void flushAndClear() {
		Session session = getSession();
		session.flush();
		session.clear();
	}

}
