package pl.keruzam.db;

import java.io.Serializable;
import java.util.List;

import jakarta.inject.Inject;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import pl.keruzam.service.cmm.lib.Dictionary;

/**
 * Bazowy obiekt repozytorium
 *
 * @author Mirek Szajowski
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractRepository implements Serializable {

	@Inject
	protected DatabaseManager dm;

	DatabaseManager getDm() {
		return dm;
	}

	public void save(final Object entity, final Object... entities) {
		dm.persist(entity, entities);
	}

	/**
	 *
	 */
	public void lock(final Class<?> entityClass, final Long id) {
		dm.lock(entityClass, id);
	}

	/**
	 * @see DatabaseManager#lock(Object)
	 */
	public void lock(final Object entity) {
		dm.lock(entity);
	}

	/**
	 * @see DatabaseManager#load(Class, Long)
	 */
	public <E> E load(final Class<E> entityClass, final Long id) {
		return dm.load(entityClass, id);
	}

	/**
	 * @see DatabaseManager#get(Class, Long)
	 */
	public <E> E get(final Class<E> entityClass, final Long id) {
		return dm.get(entityClass, id);
	}

	/**
	 * @see DatabaseManager#load(Class, Long)
	 */
	public <E> E loadAndLock(final Class<E> entityClass, final Long id) {
		return dm.loadAndLock(entityClass, id);
	}

	/**
	 * @see DatabaseManager#load(Class, Long)
	 */
	public <E> E load(final Class<E> entityClass, final Dictionary dictionary) {
		return dm.load(entityClass, dictionary.getId());
	}

	public void delete(final Object entity, final Object... entities) {
		dm.delete(entity, entities);
	}

	public void delete(final Class<?> entity, final Long id) {
		dm.delete(dm.load(entity, id));
	}

	protected Query createQuery(final String queryString) {
		return dm.createQuery(queryString);
	}

	protected Query createQuery(final String queryString, final FindCriteria criteria, final String mailAlias) {
		String alias = mailAlias == null ? "" : mailAlias + ".";
		return dm.createQuery(queryString, criteria, alias);
	}

	protected Query getNamedQuery(final String name) {
		return dm.getNamedQuery(name);
	}

	@SuppressWarnings("unchecked")
	protected <I> List<I> list(final Query query) {
		return query.list();
	}

	public Object getFirstRow(final Query query) {
		query.setMaxResults(1);
		List list = query.list();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	protected NativeQuery createQuerySql(final String query) {
		return dm.createQuerySql(query);
	}

	protected MutationQuery createMutationQuerySql(final String query) {
		return dm.createMutationQuerySql(query);
	}

	public Query getUnprotectedNamedQuery(final String name) {
		return dm.getUnprotectedNamedQuery(name);
	}

	public Query createUnprotectedQuery(final String name) {
		return dm.createUnprotectedQuery(name);
	}

	public Query createUnprotectedSessionSQLQuery(final String name) {
		return dm.getUnprotectedSession().createNativeQuery(name);
	}

	public void flush() {
		dm.flush();
	}

	public void saveAndFlush(final Object entity) {
		dm.saveAndFlush(entity);
	}

	public void saveAndClear(final Object entity) {
		dm.saveAndClear(entity);
	}

	public void flushAndClear() {
		dm.flushAndClear();
	}

	public void refresh(final Object entity) {
		dm.refresh(entity);
	}

}
