package pl.keruzam.service.cmm.context;

import java.io.Serializable;


import org.apache.commons.collections4.MultiMap;
import pl.keruzam.model.Operator;
import pl.keruzam.service.cmm.OperatorPermission;

/**
 * Obiekt kontekstu zalogowanego operatora
 *
 * @author Mirek Szajowski
 */

public interface OperatorContext extends Serializable {

	public static final String BEAN_NAME = "operatorContext";
	public static final String LAST_COMPANY = "lastCompany.id";
	public static final Long UNKNOWN = -1L;
	public static final String IS_VINDICATION_ACTIVE_FOR_APP = "vindication.isActive";



	/**
	 * Czy context jest ustawiony
	 */
	public Boolean isSetOperatorContext();


	/**
	 * Pobierz id zalogowanego operatora
	 */
	public Long getIdOperator();

	void setIdOperator(Long idOperator);

	/**
	 * Pobierz login zalogowanego operatora
	 */
	public String getLogin();


	public void setIdCurrentCompany(final Long idCurrentCompany);


	/**
	 * Czy zalogowana firma jest platnikiem VAT
	 */
	public Boolean isVatPayer();

	public boolean isSystemOperator();

	/**
	 * Sciezka do pliku z logiem firmy
	 */
	public String getFileDirAbsolutePath();

	/**
	 * Sciezka do pliku z podpisem, pieczatka firmy
	 */
	public String getFacsimileAndStampDirAbsolutePath();

	/**
	 * Sciezka do pliku z pieczecia prewencyjna
	 */
	public String getPreventiveStampDirAbsolutePath();

	public Boolean isCompanyIntegrateWithAla();

	public boolean hasRights(OperatorPermission permision);

	public Boolean wasChangeInVatPayer();

	Object get(String name);

	void setOperatorBasicData(Operator operator);

	void setOperatorContext(final Operator operator);

	/**
	 * Ustawia context operatora + wybranej firmy (id)
	 */
	void setOperatorContext(final Long idOperator, Long idCompany);


	void put(String name, Object value);


	/**
	 * Czysci kontekst zalogowanego uzytkownika
	 */
	void cleanContext();



	/**
	 * Zwraca imie i nazwisko aktualnie zalogowanego operatora
	 */
	public String getCurrentOperatorFirstAndLastName();


	/**
	 * Czy konto PODSTAWOWE - platne
	 */
	Boolean getIsPaidAccount();

	/**
	 * Czy konto DARMOWE
	 */
	Boolean getIsFreeAccount();

	/**
	 * Czy konto DEMO
	 */
	boolean getIsDemoAccount();

	/**
	 * Czy konto platne lub demo
	 */
	Boolean getIsPaidOrDemoAccount();


	/**
	 * czy operator ma prawa administratora
	 */
	boolean isAdmin();

	public MultiMap getAttachmentMap();

}
