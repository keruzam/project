package pl.keruzam.service.cmm;

import pl.keruzam.service.cmm.lib.Dictionary;

/**
 * Bazowa klasa wiersza bazodanowego
 *
 * @author Mirek Szajowski
 */

public abstract class AbstractRow extends Dictionary {

	protected Boolean selected = Boolean.FALSE;
	protected Boolean active = Boolean.TRUE;

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(final Boolean selected) {
		this.selected = selected;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

}
