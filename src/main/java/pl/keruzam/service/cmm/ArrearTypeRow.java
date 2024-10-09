package pl.keruzam.service.cmm;

import java.io.Serializable;

/**
 * Okres sprzedazy
 *
 * @author Pawel Niedzielan
 */

public class ArrearTypeRow extends AbstractRow implements Serializable {

	private String name;
	private ArrearType type;

	public ArrearTypeRow(final String name, final ArrearType type, final Long id) {
		this.name = name;
		this.type = type;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public ArrearType getType() {
		return type;
	}

	@Override
	public String getLabel() {
		return name;
	}

	@Override
	public String getAsString() {
		return name + "&" + type.name();
	}

	@Override
	public void createFromString(final String object) {
		name = object.split("&")[0];
		type = ArrearType.valueOf(object.split("&")[1]);
	}
}
