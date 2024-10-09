package pl.keruzam.service.cmm;

import java.util.HashMap;

import pl.keruzam.db.SearchCriteria;
import pl.keruzam.service.cmm.lib.FilterValueChange;

/**
 * Mapa ktora automatycznie dodaje brakujace elementy
 *
 * @author asus
 */

public class FiltersMap extends HashMap<String, Filter> {
	private final SearchCriteria searchCriteria;

	public FiltersMap(final SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Filter get(final Object key) {
		Filter filter = super.get(key);
		if (filter == null) {
			Filter value = new Filter(key.toString(), (Long) null);
			value.setFilterValueChangeListener(new FilterValueChange() {

				@Override
				public void valueChange() {
					searchCriteria.resetFirstPage();

				}
			});
			put(key.toString(), value);
			return value;
		}
		return filter;
	}
}