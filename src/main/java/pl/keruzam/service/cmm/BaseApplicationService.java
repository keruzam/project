package pl.keruzam.service.cmm;

import java.io.Serializable;
import java.util.List;

import pl.keruzam.service.cmm.lib.SortOrder;

/**
 * Bazowy interfejs serwisu aplikacyjnego
 *
 * @author Mirek Szajowski
 */
public interface BaseApplicationService<D extends AbstractDto, R extends AbstractRow> extends Serializable {

	/**
	 * Inicjalizuje dto danymi poczatkowymi
	 */
	D init(final D dto);

	/**
	 * Inicjalizuje dto danymi poczatkowymi
	 */
	D init();

	/**
	 * Pobiera DTO o zadanym id w przypadku braku wiersza wyrzuca wyjatek
	 */
	D load(Long id);

	/**
	 * Zapisuje obiekt
	 */
	Long save(D dto);

	/**
	 * Aktualizuje obiekt
	 */
	void update(D dto);

	/**
	 * Usuwa obiekt
	 */
	void delete(Long id);

	/**
	 * Zwraca liczbe wierszy w bazie danych
	 */
	int findRowsCount(Filter... filters);

	/**
	 * Zwraca liczbe wierszy w bazie danych
	 */
	int findRowsCount(String searchText, Filter... filters);

	/**
	 * Znajduje wszystkie wiersze
	 */
	List<R> findAll(Filter... filters);

	/**
	 * Znajduje posortowane wiersze
	 */
	List<R> findAll(String sortField, SortOrder sortOrder, Filter... filters);

	/**
	 * Znajduje wiersza ograniczajac liczbe zwroconych wierszy
	 */
	List<R> find(final int first, final int pageSize, Filter... filters);

	/**
	 * Znajduje wiersze dla zadanego kryterium
	 */
	List<R> find(String searchText, Filter... filters);

	/**
	 * Znajduje posortowane wiersze dla zadanego kryterium
	 */
	List<R> find(String sortField, SortOrder sortOrder, String searchText, Filter... filters);

	/**
	 * Znajduje posortowane wiersze dla zadanego kryterium ograniczajac liczbe zwroconych wierszy
	 */
	List<R> find(int first, int pageSize, String sortField, SortOrder sortOrder, String searchText, Filter... filters);

	/**
	 * Znajduje posortowane wiersze ograniczajac liczbe zwroconych wierszy
	 */
	List<R> find(int first, int pageSize, String sortField, SortOrder sortOrde, Filter... filters);

	/**
	 * Znajdue wiersz po identyfikatorze
	 */
	R findById(Long id);

	/**
	 * Zapis bez walidacji
	 */
	Long saveWithoutValidate(D dto);

	/**
	 * Update bez walidacji
	 */
	void updateWithoutValidate(D dto);

}
