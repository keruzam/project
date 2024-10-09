package pl.keruzam.service.cmm.lib;

import java.io.Serializable;

/**
 * Nasluchuje zmiany wartosci filtrow
 * 
 * @author Mirek Szajowski
 * 
 */
public interface FilterValueChange extends Serializable {
	void valueChange();
}
