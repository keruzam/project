package pl.keruzam.service.cmm;


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
