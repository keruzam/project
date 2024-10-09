package pl.keruzam.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import pl.keruzam.service.cmm.Filter;
import pl.keruzam.service.cmm.lib.SortOrder;

/**
 * Obiekt kryteriow do wyszukiwania
 *
 * @author Mirek Szajowski
 */
public class FindCriteria {

	public static final String SEARCH_FILTER = "searchFilter";
	public static final Integer UNLIMITED = -1;
	public static final int NO_RESULTS = -1;
	public static final Integer DEFAULT_PAGE_SIZE = 15;

	public static final FindCriteria EMPTY = new FindCriteria(UNLIMITED);

	Integer first;
	Integer pageSize = DEFAULT_PAGE_SIZE;
	String sortField;
	String groupField;
	SortOrder sortOrder = SortOrder.ASC;
	String searchText;
	List<Filter> filters = new ArrayList<Filter>();
	Map<String, Filter> filtersMap = new HashMap<String, Filter>();
	List<String> manualFilters = new ArrayList<String>();
	boolean isSortFieldWithoutAlias = false;

	public FindCriteria(final Integer pageSize, final Filter... filters) {
		this.first = 0;
		this.pageSize = pageSize;
		setFilters(filters);
	}

	public FindCriteria(final String searchText, final Filter... filters) {
		this.searchText = searchText;
		setFilters(filters);
	}

	public FindCriteria(final String sortField, final SortOrder sortOrder, final Filter... filters) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		setFilters(filters);
	}

	public FindCriteria(final String searchText, final String sortField, final SortOrder sortOrder, final Filter... filters) {
		this.searchText = searchText;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		setFilters(filters);
	}

	public FindCriteria(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final String searchText,
			final Filter... filters) {
		this.first = first;
		this.pageSize = pageSize;
		this.searchText = searchText;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		setFilters(filters);
	}

	public FindCriteria(final int first, final String sortField, final SortOrder sortOrder, final String searchText, final Filter... filters) {
		this.first = first;
		this.searchText = searchText;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		setFilters(filters);
	}

	public FindCriteria(final int first, final int pageSize, final String searchText, final Filter... filters) {
		this.first = first;
		this.pageSize = pageSize;
		this.searchText = searchText;
		setFilters(filters);
	}

	public FindCriteria(final int first, final String searchText, final Filter... filters) {
		this.first = first;
		this.searchText = searchText;
		setFilters(filters);
	}

	public FindCriteria(final int first, final int pageSize, final Filter... filters) {
		this.first = first;
		this.pageSize = pageSize;
		setFilters(filters);
	}

	public FindCriteria(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final Filter... filters) {
		this.first = first;
		this.pageSize = pageSize;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		setFilters(filters);
	}

	public Integer getFirst() {
		return first;
	}

	public void setFirst(final Integer first) {
		this.first = first;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(final Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(final String sortField) {
		this.sortField = sortField;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(final SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(final String searchText) {
		this.searchText = searchText;
	}

	public boolean isUnlimited() {
		return UNLIMITED.equals(this.pageSize);
	}

	public boolean isSearchTextSet() {
		return searchText != null && !searchText.isEmpty();
	}

	public String startsWithValue() {
		return "%" + searchText;
	}

	public String containsValue() {
		return "%" + searchText + "%";
	}

	public String endsWithValue() {
		return searchText + "%";
	}

	public List<Filter> getFilters() {
		return filters;
	}

	private void setFilters(final Filter... filters) {
		this.filters = Arrays.asList(filters);
		if (this.filters != null) {
			this.filtersMap = Maps.uniqueIndex(this.filters, new Function<Filter, String>() {
				@Override
				public String apply(final Filter input) {
					return input.getName();
				}
			});
		}
	}

	public Filter getFilter(final String name) {
		if (!filters.isEmpty()) {
			for (Filter filter : filters) {
				if (filter.getName().equals(name)) {
					return filter;
				}
			}
		}
		return null;
	}

	public void removeFilter(final String name) {
		if (!filters.isEmpty()) {
			filters = getFiltersWithout(name);
		}
	}

	private List<Filter> getFiltersWithout(final String name) {
		List<Filter> newFilters = new ArrayList<Filter>();
		for (Filter filter : filters) {
			if (!filter.getName().equals(name)) {
				newFilters.add(filter);
			}
		}
		return newFilters;
	}

	public boolean containsAutoFilter() {
		for (Filter filter : filters) {
			if (!isManualFilter(filter.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean isManualFilter(final String name) {
		for (String filter : manualFilters) {
			if (filter.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public FindCriteria addFilterMapping(final String src, final String target) {
		if (filtersMap.containsKey(src)) {
			filtersMap.get(src).setName(target);
		}
		return this;
	}

	public void addManualFilter(final String name) {
		manualFilters.add(name);
	}

	public String getGroupField() {
		return groupField;
	}

	public void setGroupField(final String groupField) {
		this.groupField = groupField;
	}

	public boolean isSortFieldWithoutAlias() {
		return isSortFieldWithoutAlias;
	}

	public void setSortFieldWithoutAlias(final boolean isSortFieldWithoutAlias) {
		this.isSortFieldWithoutAlias = isSortFieldWithoutAlias;
	}

}
