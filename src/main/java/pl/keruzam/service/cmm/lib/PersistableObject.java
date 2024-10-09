package pl.keruzam.service.cmm.lib;

/**
 * Klasa abstrakcyjna obiektu ktory moze zostac zapisany do bazy. Docelowy obiekt rozszezajacy ta
 * klase musi posaiadac bezparemetrowy konstruktor
 * 
 * @author Pawel Niedzielan
 * 
 */
public abstract class PersistableObject {

	public abstract String getAsString();

	public abstract void createFromString(String object);

}
