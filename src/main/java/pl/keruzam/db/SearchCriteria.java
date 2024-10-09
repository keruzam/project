package pl.keruzam.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.primefaces.model.SortOrder;
import pl.keruzam.service.cmm.Filter;
import pl.keruzam.service.cmm.FiltersMap;
import pl.keruzam.service.cmm.StringUtil;

/**
 * Kryteria wyszukiwania
 *
 * @author Mirek Szajowski
 */
public class SearchCriteria implements Serializable {

	public static final String KEY_VALUE_SEPARATOR = "=";
	public static final String CRITERIA_SEPARATOR = ";";

	private Map<String, Filter> filters = new FiltersMap(this);
	private String searchCriteria;
	private boolean isSet;
	private int first = 0;
	private int pageSize = 15;
	private pl.keruzam.service.cmm.lib.SortOrder sortOrder = pl.keruzam.service.cmm.lib.SortOrder.ASC;
	private String sortField;

	private String sortDir;
	private String criteriaString;
	private boolean isDicitionaryCriteria;

	public SearchCriteria() {

	}

	public SearchCriteria(final SearchCriteria sourceSearchCriteria) {
		this.filters = sourceSearchCriteria.getFilters();
	}

	public SearchCriteria(final String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public String getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(final String searchCriteria) {
		isSet = true;
		this.searchCriteria = searchCriteria;
	}

	public Map<String, Filter> getFilters() {
		return filters;
	}

	public boolean isSet() {
		return isSet || !filters.isEmpty();
	}

	public void resetFirstPage() {
		this.first = 0;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(final int first) {
		this.first = first;
	}

	public int getPageSize() {
		if (pageSize == 15 && criteriaString != null) {
			String findInCriteriaString = findInCriteriaString("pageSize");
			if (findInCriteriaString != null) {
				removeFormCriteriaString("pageSize");
				pageSize = Integer.valueOf(findInCriteriaString);
			}
		}
		return pageSize;
	}

	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	public pl.keruzam.service.cmm.lib.SortOrder getSortOrder() {
		return sortOrder;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(final String sortField) {
		this.sortField = sortField == null ? this.sortField : sortField;
	}

	public Filter[] getFitlers() {
		Collection<Filter> list = getEnabledFilters(filters);
		return list.toArray(new Filter[list.size()]);
	}

	private Collection<Filter> getEnabledFilters(final Map<String, Filter> filters) {
		return FluentIterable.from(filters.values()).filter(new Predicate<Filter>() {

			@Override
			public boolean apply(final Filter input) {
				return input.getValue() != null;
			}
		}).toMultiset();
	}

	public void setSortOrder(final SortOrder sortOrder, final String sortField) {
		this.sortOrder = sortOrder == null ? this.sortOrder : getSortOrder(sortOrder, sortField);
	}

	private pl.keruzam.service.cmm.lib.SortOrder getSortOrder(final SortOrder sortOrder, final String sortField) {
		if (!StringUtil.isEmpty(this.sortDir) && StringUtil.isEmpty(this.sortField)) {
			return pl.keruzam.service.cmm.lib.SortOrder.valueOf(this.sortDir);
		}
		switch (sortOrder) {
			case ASCENDING:
				return pl.keruzam.service.cmm.lib.SortOrder.ASC;
			case DESCENDING:
				return pl.keruzam.service.cmm.lib.SortOrder.DESC;
			default:
				return pl.keruzam.service.cmm.lib.SortOrder.UNSORTED;
		}
	}

	public String getSortDir() {
		return sortDir;
	}

	public void setSortDir(final String sortDir) {
		this.sortDir = sortDir;
		if (sortDir != null && !sortDir.isEmpty()) {
			this.sortOrder = pl.keruzam.service.cmm.lib.SortOrder.valueOf(this.sortDir);
		}
	}

	public void setCriteriaString(final String value) {
		this.criteriaString = value;
	}

	private String findInCriteriaString(final String key) {
		if (criteriaString != null && !criteriaString.isEmpty()) {
			String[] criterias = criteriaString.split(CRITERIA_SEPARATOR);
			for (String c : criterias) {
				if (c.startsWith(key + KEY_VALUE_SEPARATOR)) {
					return c.split(KEY_VALUE_SEPARATOR)[1];
				}
			}
		}
		return null;
	}

	private void removeFormCriteriaString(final String key) {
		if (criteriaString != null && !criteriaString.isEmpty()) {
			criteriaString = "";
			String[] criterias = criteriaString.split(CRITERIA_SEPARATOR);
			for (String c : criterias) {
				if (!c.startsWith(key + KEY_VALUE_SEPARATOR)) {
					criteriaString += c + CRITERIA_SEPARATOR;
				}
			}
		}
	}

	public boolean isDicitionaryCriteria() {
		return isDicitionaryCriteria;
	}

	public void setDicitionaryCriteria(final boolean isDicitionaryCriteria) {
		this.isDicitionaryCriteria = isDicitionaryCriteria;
	}

}
