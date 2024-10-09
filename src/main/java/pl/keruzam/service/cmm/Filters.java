package pl.keruzam.service.cmm;

import java.util.Map;

import pl.keruzam.service.cmm.lib.QueryOperator;

/**
 * 
 * @author Pawel Niedzielan
 * 
 */
public class Filters {

	private final Map<String, Filter> filters;

	public Filters(final Map<String, Filter> filters) {
		this.filters = filters;
	}

	public void setValue(final String filter, final Object value) {
		filters.get(filter).setValue(value);
	}

	public void setOperator(final String filter, final QueryOperator operator) {
		filters.get(filter).setOperator(operator);
	}

	public Map<String, Filter> getFilters() {
		return filters;
	}

	public void setValueIfEmpty(final String filterName, final Object value) {
		Filter filter = filters.get(filterName);
		if (filter.getValue() == null) {
			filter.setValue(value);
		}
	}

}
