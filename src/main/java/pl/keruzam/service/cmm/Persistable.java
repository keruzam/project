package pl.keruzam.service.cmm;

import java.util.List;

/**
 * Klasa abstrakcyjna ktora moze zostac zapisana do bazy
 * 
 * @author Pawel Niedzielan
 * 
 */
public abstract class Persistable {

	public abstract List<OperatorProfileDto> getPersistData();

}
