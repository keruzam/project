package pl.keruzam.service;

import jakarta.inject.Inject;
import pl.keruzam.db.OperatorProfileRepository;
import pl.keruzam.model.Operator;
import pl.keruzam.model.OperatorProfile;
import pl.keruzam.service.cmm.OperatorProfileDto;
import pl.keruzam.service.cmm.StringUtil;
import pl.keruzam.service.cmm.context.OperatorContext;
import pl.keruzam.service.cmm.stereotypes.ApplicationService;

/**
 * Serwis Profilu Operatora
 *
 * @author Bartek Jasik
 *
 */

@ApplicationService
public class OperatorProfileServiceImpl implements OperatorProfileService {

	@Inject
	OperatorContext operatorContext;
	@Inject
	OperatorProfileRepository repo;

	private OperatorProfileDto createOperatorProfileDto(final String keyName, final Object keyValue) {
		OperatorProfileDto dto = new OperatorProfileDto();
		dto.setKeyName(keyName);
		dto.setKeyValue(keyValue != null ? keyValue.toString() : null);
		return dto;
	}

	@Override
	public String getValue(final String key) {
		return repo.getValue(key);
	}

	@Override
	public void deleteValue(final String key) {
		OperatorProfile operatorProfile = repo.findByKeyName(key);
		if (operatorProfile != null) {
			repo.delete(OperatorProfile.class, operatorProfile.getId());
		}
	}

	@Override
	public <I> I getValue(final String key, final Class<I> clazz) {
		String value = getValue(key);
		return transform(value, clazz);
	}

	private <I> I transform(final String value, final Class<I> clazz) {
		if (!StringUtil.isEmpty(value)) {
			if (Long.class.equals(clazz)) {
				return (I) Long.valueOf(value);
			} else if (Boolean.class.equals(clazz)) {
				return (I) Boolean.valueOf(value);
			}
		}
		return null;
	}

	@Override
	public Long saveValue(final String key, final Object value) {
		return saveValue(key, value, Boolean.TRUE);
	}

	@Override
	public Long saveValue(final String key, final Object value, final Boolean whenKeyExist) {
		OperatorProfile profile = repo.findByKeyName(key);
		if (profile == null) {
			OperatorProfileDto dto = createOperatorProfileDto(key, value);
			OperatorProfile operatorProfile = new OperatorProfile(dto);
			operatorProfile.setOperator(repo.load(Operator.class, operatorContext.getIdOperator()));
			repo.save(operatorProfile);
			return operatorProfile.getId();
		} else if (whenKeyExist) {
			profile.setKeyValue(value != null ? value.toString() : null);
		}
		return profile.getId();
	}

	@Override
	public <I> I getValue(final String key, final Long idOperator, final Class<I> clazz) {
		String value = getValue(key, idOperator);
		return transform(value, clazz);
	}

	@Override
	public String getValue(final String key, final Long idOperator) {
		return repo.getValue(key, idOperator);
	}

	@Override
	public void deleteValue(final Long idOperator, final String key) {
		OperatorProfile operatorProfile = repo.findByKeyName(key, idOperator);
		if (operatorProfile != null) {
			repo.delete(OperatorProfile.class, operatorProfile.getId());
		}

	}

	@Override
	public Long saveValue(final String key, final Object value, final Long idOperator) {
		OperatorProfile profile = repo.findByKeyName(key, idOperator);
		if (profile == null) {
			OperatorProfileDto dto = createOperatorProfileDto(key, value);
			OperatorProfile operatorProfile = new OperatorProfile(dto);
			operatorProfile.setOperator(repo.cleanLoad(Operator.class, idOperator));
			repo.cleanSave(operatorProfile);
			return operatorProfile.getId();
		} else {
			profile.setKeyValue(value != null ? value.toString() : null);
			repo.cleanSave(profile);
		}
		return profile.getId();
	}
}
