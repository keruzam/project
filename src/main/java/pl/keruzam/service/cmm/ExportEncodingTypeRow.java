package pl.keruzam.service.cmm;

/**
 * Row dla typów kodowanai przy eksporcie
 *
 * @author Marcel Matuszak
 */

public class ExportEncodingTypeRow extends AbstractRow {

	private final String name;
	private final String value;

	public ExportEncodingTypeRow(final String name, final String value, final Long id) {
		this.name = name;
		this.value = value;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String getAsString() {
		return name;
	}

	@Override
	public String getLabel() {
		return name;
	}
}
