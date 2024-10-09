package pl.keruzam.service;

import java.io.Serializable;
import java.util.List;

/**
 * Serwis Aplikacyjny Profilu Aplikacji
 *
 * @author Paweł Niedzielan
 */
public interface ApplicationProfileService extends Serializable {

	/**
	 * Zapis klucza oraz wartości
	 */
	Long saveValue(String key, Object value);

	/**
	 * Zapis klucza, wartości oraz informacji czy klucz istnieje
	 */
	Long saveValue(String key, Object value, Boolean whenKeyExist);

	/**
	 * Usunięcie wartości po kluczu
	 */
	void deleteValue(final String key);

	/**
	 * Popbranie wartosci z profilu firmy za pomocą klucza
	 */
	String getValue(String key);

	/**
	 * Popbranie wartosci z profilu firmy za pomocą klucza z jednoczesnym rzutowaniem na obiekt class-y
	 */
	<I> I getValue(String key, Class<I> clazz);

	/**
	 * Pobiera wartosci na zasadzie klucz-wartosc
	 */
	List<String[]> getValues(String keyLike);

	List<String[]> executeSQL(String sql);

	/**
	 * Określa jest jest aktywna kontrola domen w aplikacji
	 */
	public Boolean isDomainCheckerActive();
}
