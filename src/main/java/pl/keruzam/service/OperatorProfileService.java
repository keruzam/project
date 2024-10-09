package pl.keruzam.service;

import java.io.Serializable;

/**
 * Serwis Aplikacyjny Profilu Operatora
 *
 * @author Bartek Jasik
 */
public interface OperatorProfileService extends Serializable {

	public static final String SEND_ME_EMAIL_COPY_KEY = "print.email.sendMeEmailCopy";

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
	 * Popbranie profilu operatora za pomocą klucza
	 */
	String getValue(String key);

	/**
	 * Pobranie profilu operatora za pomocą klucza z jednoczesnym rzutowaniem na obiekt class-y
	 */
	<I> I getValue(String key, Class<I> clazz);

	/**
	 * Pobranie profilu operatora z argumentu metody za pomocą klucza z jednoczesnym rzutowaniem na obiekt class-y
	 */

	<I> I getValue(String key, Long idOperator, Class<I> clazz);

	/**
	 * Usunięcie wartości po kluczu i operatorze
	 */
	void deleteValue(final Long idOperator, final String key);

	/**
	 * 		Pełny zapis bez dopełniania danych kontekstowych
	 */
	@Deprecated
	Long saveValue(String key, Object value, Long idOperator);

	/**
	 * Pobranie klucza profilu operatora obiekt class-y
	 */

	String getValue(String key, Long idOperator);

}
