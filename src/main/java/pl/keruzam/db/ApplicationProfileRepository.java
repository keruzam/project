package pl.keruzam.db;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import pl.keruzam.model.ApplicationProfile;
import pl.keruzam.service.cmm.stereotypes.Repository;

/**
 * Repository profilu aplikacji
 *
 * @author Pawel Niedzielan
 */
@Repository
public class ApplicationProfileRepository extends AbstractRepository {

	public String getValue(final String keyName) {
		Query query = dm.getNamedQuery("Common.findApplicationProfileValueByKeyName");
		query.setParameter("keyName", keyName);
		return (String) query.uniqueResult();
	}

	public ApplicationProfile findByKeyName(final String keyName) {
		Query query = dm.getNamedQuery("Common.findApplicationProfileByKeyName");
		query.setParameter("keyName", keyName);
		return (ApplicationProfile) query.uniqueResult();
	}

	public List<ApplicationProfile> findByKeyNameLike(final String keyName) {
		Query query = dm.getNamedQuery("Common.findApplicationProfileByKeyNameLike");
		query.setParameter("keyName", keyName);
		return query.list();
	}

	public NativeQuery createQuerySql(String sql) {
		return dm.createQuerySql(sql);
	}

}
