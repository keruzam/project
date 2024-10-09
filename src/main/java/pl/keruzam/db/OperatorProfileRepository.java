package pl.keruzam.db;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;
import io.micrometer.common.lang.Nullable;
import jakarta.inject.Inject;
import pl.keruzam.model.Operator;
import pl.keruzam.model.OperatorProfile;
import pl.keruzam.service.cmm.context.OperatorContext;
import pl.keruzam.service.cmm.stereotypes.Repository;

/**
 * Repository profilu operatora
 *
 * @author Bartek Jasik
 */
@Repository
public class OperatorProfileRepository extends AbstractRepository {

	@Inject
	OperatorContext operatorContext;

	@Nullable
	public OperatorProfile findByKeyName(final String keyName) {
		return findByKeyName(keyName, operatorContext.getIdOperator());
	}

	@Nullable
	public OperatorProfile findByKeyName(final String keyName, final Long idOperator) {
		Map<String, Serializable> map = Maps.newHashMap();
		map.put(OperatorProfile.FIELD_KEY_NAME, keyName);
		map.put(OperatorProfile.FIELD_OPERATOR, dm.load(Operator.class, idOperator));
		OperatorProfile loadbyNaturalId = dm.loadbyNaturalId(OperatorProfile.class, map);
		return loadbyNaturalId;
	}

	@Nullable
	public String getValue(final String keyName) {
		OperatorProfile findByKeyName = findByKeyName(keyName);
		if (findByKeyName == null) {
			return null;
		}
		return findByKeyName.getKeyValue();
	}

	@Nullable
	public String getValue(final String keyName, final Long idOperator) {
		OperatorProfile findByKeyName = findByKeyName(keyName, idOperator);
		if (findByKeyName == null) {
			return null;
		}
		return findByKeyName.getKeyValue();
	}

	@Deprecated
	public void cleanSave(final OperatorProfile operatorProfile) {
		dm.getUnprotectedSession().persist(operatorProfile);
	}

	@Deprecated
	public <E> E cleanLoad(final Class<E> clazz, final Long id) {
		return (E) dm.getUnprotectedSession().load(clazz, id);
	}
}
