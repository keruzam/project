package pl.keruzam.service.cmm.lib;

import java.io.Serializable;

/**
 * Klasa slownika
 *
 * @author Mirek Szajowski
 */

public class Dictionary extends PersistableObject implements Serializable {
	public static final transient String ID_NAME = "id";

	protected Long id;
	protected String label;

	public Dictionary() {
	}

	public Dictionary(final Long id, final String label) {
		this.id = id;
		this.label = label;
	}

	public Dictionary(final Long id) {
		this.id = id;
	}

	public Dictionary(final String label) {
		this.label = label;
	}

	public static final Long getValue(final Dictionary dictionaryObject) {
		return dictionaryObject != null ? dictionaryObject.getId() : null;
	}

	public static final String getLabel(final Dictionary dictionaryObject) {
		return dictionaryObject != null ? dictionaryObject.getLabel() : null;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Dictionary) {
			if (id != null) {
				return id.equals(((Dictionary) obj).getId());
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		}
		return super.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(super.hashCode());
	}

	@Override
	public String getAsString() {
		return getId() != null ? getId().toString() : null;
	}

	@Override
	public void createFromString(final String object) {
		this.id = Long.valueOf(object);
	}
}
