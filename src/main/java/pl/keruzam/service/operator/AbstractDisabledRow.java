package pl.keruzam.service.operator;

import pl.keruzam.service.cmm.AbstractRow;

/**
 * Klasa wiersza posiadajacego atrybut isDisabled
 * 
 * @author marcel.wojtczak
 * 
 */
public class AbstractDisabledRow extends AbstractRow {

	protected Boolean isDisabled = Boolean.TRUE;

	public Boolean getIsDisabled() {
		return isDisabled;
	}

}
