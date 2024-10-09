package pl.keruzam.service.cmm;

import pl.keruzam.service.cmm.lib.NotNull;

/**
 * Dto Profilu Operatora
 *
 * @author Bartek Jasik
 */

public class OperatorProfileDto extends AbstractDto {

	Long id;
	@NotNull
	String keyName;
	@NotNull
	String keyValue;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(final String keyName) {
		this.keyName = keyName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(final String keyValue) {
		this.keyValue = keyValue;
	}

}
