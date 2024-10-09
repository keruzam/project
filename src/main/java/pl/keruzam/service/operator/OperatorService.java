package pl.keruzam.service.operator;

import pl.keruzam.service.cmm.BaseApplicationService;
import pl.keruzam.service.cmm.lib.AuthenticationException;

/**
 * Interfejs serwisu Operatora
 *
 * @author Mirek Szajowski
 */

public interface OperatorService extends BaseApplicationService<OperatorDto, OperatorRow> {

	String IP_ADDRESS_KEY = "operatorIpAddress";

	/**
	 * Logowanie
	 */
	void login(String email, String password) throws AuthenticationException;


	/**
	 * Sprawdzanie hasla
	 */
	boolean checkPassword(String email, String password);

	/**
	 * Wyszukiwanie operatora po adresie email. Zwraca dto operatora.
	 */
	OperatorDto findByEmail(String email);

	/**
	 * Ustawia context dla operatora
	 */
	OperatorDto setOperatorContext(String email);

	/**
	 * Ustawia context dla operatora, zapisuje ip operatora w operator.last_ip_address
	 */
	OperatorDto setOperatorContext(String login, String ipAddress);

	/**
	 * Ustawia id aktywnej firmy
	 */
	void setCurrentCompany(Long idCurrentCompany);

	/**
	 * Zmiana hasla na wygenerowane losowo. Wysylanie maila z linkiem aktywacyjnym.
	 */
	void generatePassword(String email);

	/**
	 * Aktywuje wygenerowane haslo. Zapisuje je w profilu uzytkownika.
	 */
	void activateGeneratedPassword(final String login, String hash);

	/**
	 * Aktywuj konto (link aktywacyjny)
	 */
	void activateAccount(String login, String hash);

	/**
	 * Zmiana hasła zalogowanego operatora
	 */
	void changePassword(String oldPassword, String newPassword);

	/**
	 * Zmiana hasła zalogowanego operatora wynikajaca ze zmiany adresu e-mail
	 */
	void changePassword(String oldPassword, String newPassword, String newEmail);

	/**
	 * Zmiana hasla i emaila zalogowanego operatora
	 */
	void changeLoginOperatorPassAndEmail(String newEmail, String oldEmail, String newPassword, String oldPassword);

	/**
	 * Zmiana maila wybranego operatora
	 */
	void changeSelectedDtoEmail(OperatorDto dto);

	/**
	 * Sprawdza czy operator jest właścicielem konta
	 */
	Boolean isAccountOwner(Long idOperator);

	/**
	 * Sprawdza czy konto operatora jest aktywne
	 */
	Boolean isOperatorAccountActive(Long idOperator);



	Long findAccount(String login);

	/**
	 * Sprawdza czy jest mozliwe dodanie kolejnego operatora
	 */
	Boolean getIsAbleToAddOperator();




	/**
	 * Ustawia pierwsza dostepna firme jezeli zapamietana firma wygasla, zwraca 'true' jezeli nastapila zmiana na dostepna firme
	 */
	Boolean setFirtAvailabeCompany(Long idOperator);

	/**
	 * Znajduje głównego operatora po id firmy
	 */
	OperatorDto loadAdminOperatorByIdCompany(Long idCompany);

	void activateAccount(String email);

	/**
	 * Wyszukuje id operatora po nazwisku i imieniu w obrebie konta
	 */
	Long findIdByName(Long idAccount, String lastName, String firstName);

	/**
	 * Znajduje głównego operatora dla konta
	 */
	OperatorDto findMainOperatorForAccount(Long idAccount);

	/**
	 * Update podstawowych danych operatora (imie, nazwisko, email)
	 */
	void updateBasicData(OperatorDto dto);




	/**
	 * Wyloguj operatorContext.cleanContext()
	 */
	void logout();



	String getTestText();
}
