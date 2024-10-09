package pl.keruzam.service.cmm;

/**
 * Uprawnienia użytkownika
 * 
 * @author Tomasz Mazurek
 * 
 */
public enum OperatorRole {
	/**
	 * Uzytkownik konta
	 */
	USER,
	/**
	 * Uzytkownik konta z prawami admina
	 */
	ADMIN,
	/**
	 * Uzytkownik ktory moze przegladac logi uzywac ukrytych opcji
	 */
	SUPER_USER;
}