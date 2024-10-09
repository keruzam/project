package pl.keruzam.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.inject.Inject;
import org.hibernate.query.NativeQuery;
import pl.keruzam.db.ApplicationProfileRepository;
import pl.keruzam.model.ApplicationProfile;
import pl.keruzam.service.cmm.StringUtil;
import pl.keruzam.service.cmm.stereotypes.ApplicationService;

/**
 * Serwis Profilu Applikacji
 *
 * @author Pawel Niedzielan
 *
 */
@ApplicationService
public class ApplicationProfileServiceImpl implements ApplicationProfileService {

	@Inject
	ApplicationProfileRepository repo;

	@Override
	public String getValue(final String key) {
		String value = repo.getValue(key);
		return value;
	}

	@Override
	public void deleteValue(final String key) {
		ApplicationProfile profile = repo.findByKeyName(key);
		repo.delete(ApplicationProfile.class, profile.getId());
	}

	@Override
	public <I> I getValue(final String key, final Class<I> clazz) {
		try {
			String value = getValue(key);
			if (!StringUtil.isEmpty(value)) {
				return clazz.getConstructor(String.class).newInstance(value);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public Long saveValue(final String key, final Object value) {
		return saveValue(key, value, Boolean.TRUE);
	}

	@Override
	public Long saveValue(final String key, final Object value, final Boolean whenKeyExist) {
		ApplicationProfile profile = repo.findByKeyName(key);
		if (profile == null) {
			profile = new ApplicationProfile();
			profile.setKeyName(key);
			profile.setKeyValue(value != null ? value.toString() : null);
			repo.save(profile);
			return profile.getId();
		} else if (whenKeyExist) {
			profile.setKeyValue(value != null ? value.toString() : null);
		}
		return profile.getId();
	}

	@Override
	public List<String[]> getValues(final String keyLike) {
		List<String[]> result = new ArrayList<String[]>();
		List<ApplicationProfile> findByKeyNameLike = repo.findByKeyNameLike(keyLike);
		for (ApplicationProfile profile : findByKeyNameLike) {
			result.add(new String[] { profile.getKeyName(), profile.getKeyValue() });
		}
		return result;
	}



	@Override
	public List<String[]> executeSQL(final String sql) {
		List<String[]> result = new ArrayList<String[]>();
		NativeQuery sqlQuery = repo.createQuerySql(sql);
		sqlQuery.setCacheable(false);
		List<Object> list = sqlQuery.list();
		for (Object element : list) {
			Object[] row = new Object[] { element };
			if (element.getClass().isArray()) {
				row = (Object[]) element;
			}
			String[] r = new String[row.length];
			for (int i = 0; i < row.length; i++) {
				if (row[i] != null) {
					r[i] = row[i].toString();
				} else {
					r[i] = "";
				}
			}
			result.add(r);
		}
		return result;
	}

	@Override
	public Boolean isDomainCheckerActive() {
		return getValue(ApplicationProfile.IS_DOMAIN_CHECKER_ACTIVE, Boolean.class);
	}

}
