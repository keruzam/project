package pl.keruzam.service.operator;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import jakarta.annotation.PostConstruct;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.component.visit.VisitCallback;
import jakarta.faces.component.visit.VisitContext;
import jakarta.faces.component.visit.VisitResult;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.myfaces.component.visit.FullVisitContext;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import pl.keruzam.db.AnalysisSource;
import pl.keruzam.db.SearchCriteria;
import pl.keruzam.db.SearchCriteriaDelegate;
import pl.keruzam.service.OperatorContextWww;
import pl.keruzam.service.ResourceService;
import pl.keruzam.service.cmm.AbstractRow;
import pl.keruzam.service.cmm.ArrearTypeProvider;
import pl.keruzam.service.cmm.ArrearTypeRow;
import pl.keruzam.service.cmm.ExportEncodingTypeRow;
import pl.keruzam.service.cmm.ExportHelper;
import pl.keruzam.service.cmm.Filter;
import pl.keruzam.service.cmm.Filters;
import pl.keruzam.service.cmm.StringUtil;
import pl.keruzam.service.cmm.lib.Date;
import pl.keruzam.service.cmm.lib.Decimal;
import pl.keruzam.service.cmm.lib.Dictionary;
import pl.keruzam.service.cmm.lib.PersistableObject;

/**
 * Bazowy dostawca danych. Metody ktore nalezy pokryc "find" oraz opcjonlanie "getRowCount" gdy przewidujemy stronnicowanie tabeli
 *
 * @author Mirek Szajowski
 */
public abstract class AbstractDataProvider<R extends AbstractRow> extends LazyDataModel<R> {

	public static final String DOCUMENT_PERIOD = "documentPeriod";
	public static final String FILTER_COOKIE_PREFIX = "filter_";
	protected static final int UNLIMITED_PAGE_SIZE = 1000;
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final String ID_PARAM = "id";
	private static final int MAX_AUTOCOMPLETE_ROWS = 100;
	private static final int NOT_OVERRIDED = -1;
	private static final Logger logger = LoggerFactory.getLogger(AbstractDataProvider.class);
	@Inject
	@Lazy
	protected OperatorContextWww operatorContextWww;
	@Inject
	@Lazy
	protected ResourceService resourceService;
	protected AnalysisSource analysisSource;
	protected List<R> list = Collections.emptyList();
	protected List<List<String>> summaryList = new ArrayList<List<String>>();
	protected List<List<String>> summaryUnderTable = new ArrayList<List<String>>();
	//	@Inject
	//	OperatorService operatorService;
	int currentRowCount = 0;
	Map<Long, R> elementsById = new HashMap<Long, R>();
	Map<String, R> elementsByLabel = new HashMap<String, R>();
	String localSearch;
	//	@Inject
	//	private BeanManager bm;
	private R[] selected;
	private Set<String> selectedGridSet = new LinkedHashSet<String>();
	private Boolean exportOnlySelectedRow = Boolean.FALSE;
	private ExportEncodingTypeRow exportEncodingTypeRow;
	private boolean dataChanged = true;
	private Class rowClass;
	private boolean afterInitialize;
	private String clientId;
	private String exportFileName;

	public AbstractDataProvider() {
		try {
			Class<?> genericSource = findGenericSourceClass(getClass());
			rowClass = (Class) ((ParameterizedType) genericSource.getGenericSuperclass()).getActualTypeArguments()[0];
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setRowIndex(final int rowIndex) {
		if (rowIndex != -1 && getPageSize() == 0) {
			setPageSize(DEFAULT_PAGE_SIZE);
			if (rowIndex == -1) {
				super.setRowIndex(0);
			} else {
				super.setRowIndex(rowIndex);
			}
		} else {
			super.setRowIndex(rowIndex);
		}
	}

	protected Class<?> findGenericSourceClass(final Class<?> clazz) {
		Class<?> superclass = clazz.getSuperclass();
		if (superclass.equals(AbstractDataProvider.class)) {
			return clazz;
		}
		return findGenericSourceClass(superclass);
	}

	@PostConstruct
	public void initializeDataProvider() {
		afterInitialize = true;
		//getFilters().clear();
		if (isRememberFilters()) {
			Map<String, Filter> savedFilter = null;//(Map<String, Filter>) operatorContextWww.getCurrentFilters().get(getFilterPage());
			if (isSaveDefaultOperatorFilters() && savedFilter == null) {
				defaultOperatorFilters(new Filters(getFilters()));
			}
			if (savedFilter != null) {
				getFilters().putAll(savedFilter);
			} else {
				defaultFiltersValues(new Filters(getFilters()));
			}
		} else {
			defaultFiltersValues(new Filters(getFilters()));
		}
		exportEncodingTypeRow = new ExportEncodingTypeRow("Windows-1250", "Windows-1250", 1L);
	}

	public List<R> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final Map<String, Object> filters) {
		SearchCriteria criteria = new SearchCriteria(localSearch);
		if (saveUserSearchCriteria()) {
			criteria = getSearchCriteriaObject();
		}

		criteria.setFirst(first);
		criteria.setPageSize(pageSize);

		if (!afterInitialize) {
			criteria.setSortField(sortField);
			criteria.setSortOrder(sortOrder, sortField);
			rememberSort(sortField, criteria.getSortOrder());
			if (criteria != null && criteria.getFilters() != null) {
				if (isRememberFilters()) {
					saveOperatorFilter(criteria);
				}
			}
		} else {
			afterInitialize = false;
			criteria.setFirst(0);
		}

		list = findByCriteria(criteria);

		createRowsState(list);
		dataChanged = true;
		return list;
	}

	// XXX jesli sortowanie bedzie zapamietywane w bazie to to mozna wyrzucic
	protected void rememberSort(final String sortField, final pl.keruzam.service.cmm.lib.SortOrder sortOrder) {

	}

	protected boolean saveUserSearchCriteria() {
		return true;
	}

	private List<R> findByCriteria(final SearchCriteria criteria) {
		return find(criteria.getFirst(), criteria.getPageSize(), criteria.getSortField(), criteria.getSortOrder(), criteria.getFitlers(),
				criteria.getSearchCriteria());
	}

	private void createRowsState(final List<R> rows) {
		elementsById.clear();
		for (R row : rows) {
			elementsById.put(row.getId(), row);
			elementsByLabel.put(row.getLabel(), row);
		}
	}

	public AbstractRow findByLabel(final String label) {
		if (label == null) {
			return null;
		}
		return elementsByLabel.get(label);
	}

	public List<R> complete(final String search) {
		SearchCriteria criteria = new SearchCriteria(
				this.getSearcher().getSearchCriteria(this, operatorContextWww.getIdCurrentCompany(), isDicitionaryProvider()));
		criteria.setSearchCriteria(search);
		criteria.setPageSize(MAX_AUTOCOMPLETE_ROWS);
		if (sortFieldForBigDictionary() != null) {
			criteria.setSortField(sortFieldForBigDictionary());
		}
		List<R> value = findByCriteria(criteria);
		List<R> activeList = getActiveList();
		return activeList;
	}

	/**
	 * Ustawia pole sortowania dla metody complate (wykorzystywane w bigFilter)
	 */
	protected String sortFieldForBigDictionary() {
		return null;
	}

	/**
	 * Metoda wyrzukujaca do pokrycia
	 */
	protected abstract List<R> find(final int first, final int pageSize, final String sortField, final pl.keruzam.service.cmm.lib.SortOrder sortOrder,
			final Filter[] filters, final String searchText);

	@Override
	public String getRowKey(final R object) {
		return object.getId().toString();
	}

	@Override
	public R getRowData() {
		try {
			return super.getRowData();
		} catch (Exception e) {
			logger.error("Primefaces bug " + e.getMessage());
		}
		return null;
	}

	@Override
	public R getRowData(final String rowKey) {
		Preconditions.checkNotNull(rowKey);
		R row = null;
		try {
			Long id = Long.valueOf(rowKey);
			row = elementsById.get(id);
			if (row == null) {
				row = findByInDatabase(id);
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return row;
	}

	private R findByInDatabase(final Long id) {
		SearchCriteria searchCriteria = new SearchCriteria();
		endableIdFiler(searchCriteria, id);
		List<R> result = findByCriteria(searchCriteria);
		createRowsState(result);
		return elementsById.get(id);
	}

	private void invokeDbRequest() {
		load(0, UNLIMITED_PAGE_SIZE, null, SortOrder.UNSORTED, null);
	}

	private void endableIdFiler(final SearchCriteria criteria, final Long id) {
		criteria.getFilters().get(AbstractRow.ID_NAME).setValue(id);
	}

	@Override
	public int getRowCount() {
		if (dataChanged) {
			int pageSize = setPageSize();
			if (pageSize > list.size()) {
				currentRowCount = list.size() + getSearchCriteriaObject().getFirst();
			} else {
				currentRowCount = getRowCount(getSearchText());
			}
			if (getSearchCriteriaObject().getFirst() == 0) {
				PrimeFaces.current().executeScript("synchronizeTablePaginatorState('" + clientId + "')");
			}
			dataChanged = false;
		}
		if (NOT_OVERRIDED == currentRowCount) {
			currentRowCount = list.size();
		}
		return currentRowCount;
	}

	private String getSearchText() {
		return getSearchCriteriaObject().getSearchCriteria();
	}

	private int setPageSize() {
		int pageSize = getPageSize() == 0 ? UNLIMITED_PAGE_SIZE : getPageSize();
		setPageSize(pageSize);
		return pageSize;
	}

	/**
	 * Meoda ktora zwraca liste wierszy.
	 *
	 * @param filter
	 * @return
	 */
	protected int getRowCount(final String filter) {
		return NOT_OVERRIDED;
	}

	public void search() {
		clearPageNumber();
	}

	public void clearAll() {
		clearSearchText();
		clearPageNumber();
		clearFilters();
		if (isRememberFilters()) {
			clearOperatorFilters();
		}
		defaultFiltersValues(new Filters(getFilters()));
	}

	private void clearFilters() {
		for (Filter filter : getSearchCriteriaObject().getFilters().values()) {
			filter.setValue(null);
		}
	}

	private SearchCriteria getSearchCriteriaObject() {
		return getSearcher().getSearchCriteria(this, operatorContextWww.getIdCurrentCompany(), isDicitionaryProvider());
	}

	private void clearPageNumber() {
	}

	public void clearSearchText() {
		getSearchCriteriaObject().setSearchCriteria(null);
		this.localSearch = null;
	}

	public String getSearchCriteria() {
		SearchCriteria criteria = new SearchCriteria(localSearch);
		if (saveUserSearchCriteria()) {
			criteria = getSearchCriteriaObject();
		}
		return criteria.getSearchCriteria();
	}

	public void setSearchCriteria(final String searchCriteria) {
		this.localSearch = searchCriteria;
		if (saveUserSearchCriteria()) {
			getSearchCriteriaObject().setSearchCriteria(searchCriteria);
		}
	}

	public int getFirstPage() {
		return getSearchCriteriaObject().getFirst();
	}

	@Override
	public int getPageSize() {
		return getSearchCriteriaObject().getPageSize();
	}

	public AbstractDataProvider getProvider(final String sortColumn, final String sortDir) {
		SearchCriteria searchCriteria = getSearchCriteriaObject();
		if (searchCriteria.getSortDir() == null) {
			searchCriteria.setSortDir(sortDir);
		}

		if (searchCriteria.getSortField() == null) {
			searchCriteria.setSortField(sortColumn);
		}

		//Bean<?> next = bm.getBeans(this.getClass()).iterator().next();
		//Object reference = bm.getReference(next, this.getClass(), null);
		//return (AbstractDataProvider) reference;
		return null;
	}

	public void setSortField(final String sortField) {
		getSearchCriteriaObject().setSortField(sortField);
	}

	public void setSortOrder(final String sortOrder) {
		getSearchCriteriaObject().setSortDir(sortOrder);
	}

	public String getCurrentSortField() {
		return getSearchCriteriaObject().getSortField();
	}

	public String getCurrentSortOrder() {
		pl.keruzam.service.cmm.lib.SortOrder order = getSearchCriteriaObject().getSortOrder();
		return order != null ? order.name() : "";
	}

	public List<R> getList() {
		if (list.isEmpty()) {
			invokeDbRequest();
		}
		return list;
	}

	public int getListSize() {
		return getList().size();
	}

	public List<R> getActiveList() {
		return getActiveList();
	}

	public void setActiveList(final List<R> activeList) {
		this.list = activeList;
	}



	public Map<String, Filter> getFilters() {
		return getSearchCriteriaObject() == null ? null : getSearchCriteriaObject().getFilters();
	}



	public List<Long> getSelectedGridSetId() {
		List<Long> list = new ArrayList<Long>();
		for (String value : selectedGridSet) {
			list.add(Long.valueOf(value));
		}
		return list;
	}

	/**
	 * Pobiera aktualny identyfikator(id)
	 */
	public Long getId() {
		Long id = null;
		String string = getParam(ID_PARAM);
		if (string != null) {
			id = Long.valueOf(string);
		}
		return id;
	}

	/**
	 * Pobiera parametr o podanej nazwie
	 */
	public <I> I getParam(final String name, final Class<I> type) {
		try {
			String param = getParam(name);
			if (!StringUtil.isEmpty(param)) {
				return type.getConstructor(String.class).newInstance(param);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * Pobiera parametr o podanej nazwie
	 */
	public String getParam(final String name) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	}

	public void searchAll() {
		clearAll();
	}

	protected void defaultFiltersValues(final Filters filters) {

	}

	public void clearList() {
		list.clear();
	}

	public SearchCriteriaDelegate getSearcher() {
		return (SearchCriteriaDelegate) operatorContextWww.get(SearchCriteriaDelegate.class);
	}

	/**
	 * Czy prowider wylacznie slownikowy (ustawione na true nie pobiera ani nie zapisuje kryteriow do bazy)
	 */
	protected boolean isDicitionaryProvider() {
		return false;
	}

	public void setClientId(final String clientId) {
		this.clientId = clientId;
	}

	public String prepareExportFileName(final String name) {
		if (name.contains(" ")) {
			return StringUtil.replacePolishChars(name.replace(" ", "_"));
		}
		return StringUtil.replacePolishChars(name);
	}

	protected void removeFiltersBeforeExport(final SearchCriteria criteria) {

	}

	public void exportPDF(final String idTable) throws IOException {

	}

	public void exportExcel(final String idTable) throws IOException {
		//		FacesContext context = FacesContext.getCurrentInstance();
		//		DataTable table = getTable(idTable, context);
		//		SearchCriteria criteria = new SearchCriteria(localSearch);
		//		if (saveUserSearchCriteria()) {
		//			criteria = getSearchCriteriaObject();
		//		}
		//		int pageSize = getPageSize();
		//		Exporter excelExport = new CustomExcelExporter();
		//		excelExport.export(context, table, prepareExportFileName(exportFileName), Boolean.FALSE, exportOnlySelectedRow, exportEncodingTypeRow.getValue(), null,
		//				null);
		//		criteria.setPageSize(pageSize == 0 ? DEFAULT_PAGE_SIZE : pageSize);
		//		context.responseComplete();
	}

	public void exportCSV(final String idTable) throws IOException {
		//		FacesContext context = FacesContext.getCurrentInstance();
		//		DataTable table = getTable(idTable, context);
		//		SearchCriteria criteria = new SearchCriteria(localSearch);
		//		if (saveUserSearchCriteria()) {
		//			criteria = getSearchCriteriaObject();
		//		}
		//		int pageSize = getPageSize();
		//		Exporter csvExport = new CustomCSVExporter();
		//		csvExport.export(context, table, prepareExportFileName(exportFileName), Boolean.FALSE, exportOnlySelectedRow, exportEncodingTypeRow.getValue(), null,
		//				null);
		//		criteria.setPageSize(pageSize == 0 ? DEFAULT_PAGE_SIZE : pageSize);
		//		context.responseComplete();
	}

	protected ExportHelper createExportHelper(final String idTable, final FacesContext context) {
		DataTable table = getTable(idTable, context);
		OperatorDto operatorDto = null; //operatorService.load(operatorContextWww.getIdOperator());
		Map<String, String> filtersForExport = createFiltersForExport();

		ExportHelper exportHelper = new ExportHelper();
		exportHelper.setTable(table);
		exportHelper.setContext(context);
		exportHelper.setFilename(prepareExportFileName(exportFileName));
		exportHelper.setPostProcessor(null);
		exportHelper.setPreProcessor(null);
		exportHelper.setAuthor(operatorDto.getFirstName() + " " + operatorDto.getLastName());
		exportHelper.setFilters(filtersForExport);
		exportHelper.setSummaryList(summaryList);
		exportHelper.setSelectionOnly(exportOnlySelectedRow);
		exportHelper.setCompany(""); //operatorContextWww.getCurrentCompanyFullName());
		exportHelper.setEncodingType(exportEncodingTypeRow.getValue());
		exportHelper.setTitle(exportFileName);

		return exportHelper;
	}

	public ExportEncodingTypeRow getExportEncodingTypeRow() {
		return exportEncodingTypeRow;
	}

	public void setExportEncodingTypeRow(final ExportEncodingTypeRow exportEncodingTypeRow) {
		this.exportEncodingTypeRow = exportEncodingTypeRow;
	}

	private Map<String, String> createFiltersForExport() {
		Map<String, String> filtersForExport = new HashMap<String, String>();

		putSearchText(filtersForExport);
		putFilters(filtersForExport);

		return filtersForExport;
	}

	protected void putFilters(final Map<String, String> filtersForExport) {
		SearchCriteria criteria = getSearchCriteriaObject();
		Filter[] filters = criteria.getFitlers();

		for (Filter filter : filters) {
			if (filter.getValue() instanceof AbstractRow) {
				putAbstractRowValue(filtersForExport, filter);
			} else if (filter.getValue() instanceof Boolean) {
				putBooleanValue(filtersForExport, filter);
			} else if (filter.getValue() instanceof String) {
				filtersForExport.put(getMessage(filter.getName()), (String) filter.getValue());
			} else if (filter.getValue() instanceof Integer) {
				putIntegerValue(filtersForExport, filter);
			}
		}
	}

	private void putIntegerValue(final Map<String, String> filtersForExport, final Filter filter) {
		Integer value = (Integer) filter.getValue();
		filtersForExport.put(getMessage(filter.getName()), value.toString());
	}

	private void putBooleanValue(final Map<String, String> filtersForExport, final Filter filter) {
		String is = ((Boolean) filter.getValue()) ? "Tak" : "Nie";
		filtersForExport.put(getMessage(filter.getName()), is);
	}

	protected void putAbstractRowValue(final Map<String, String> filtersForExport, final Filter filter) {
		AbstractRow abstractRow = (AbstractRow) filter.getValue();
		filtersForExport.put(getMessage(filter.getName()), abstractRow.getLabel());
	}

	protected String getMessage(final String value) {
		return "";//resourceService.getMessage(value);
	}

	private void putSearchText(final Map<String, String> filtersForExport) {
		String text = getSearchCriteria();
		if (!StringUtil.isEmpty(text)) {
			filtersForExport.put("Szukana fraza", text);
		}
	}

	private DataTable getTable(final String id, final FacesContext context) {
		UIViewRoot root = context.getViewRoot();
		final UIComponent[] found = new UIComponent[1];
		root.visitTree(new FullVisitContext(context), new VisitCallback() {

			@Override
			public VisitResult visit(final VisitContext context, final UIComponent component) {
				if (component.getId().equals(id)) {
					found[0] = component;
					return VisitResult.COMPLETE;
				}
				return VisitResult.ACCEPT;
			}
		});

		return (DataTable) found[0];
	}

	public String getExportFileName() {
		return exportFileName + "_" + new Date();
	}

	public void setExportFileName(final String exportFileName) {
		String convertedExportFileName = exportFileName.replaceAll("\\s", "_");
		this.exportFileName = convertedExportFileName;
	}

	public String getExportFileNameWithoutDate() {
		return exportFileName;
	}

	protected List<String> addToList(final Object value, final List<String> list) {
		return addToList(value, list, Boolean.TRUE);
	}

	protected List<String> addToList(final Object value, final List<String> list, final Boolean isZeroDisplay) {
		return addToList(value, list, isZeroDisplay, Boolean.FALSE);
	}

	protected List<String> addToList(final Object value, final List<String> list, final Boolean isZeroDisplay, final Boolean isAutoScaleDecimal) {
		if (value != null) {
			if (value instanceof Decimal) {
				Decimal valueDecimal = (Decimal) value;
				Decimal zero = new Decimal(0);

				if (!(!valueDecimal.isGreater(zero) && !isZeroDisplay)) {
					String valueAsString = isAutoScaleDecimal ? valueDecimal.toStringWithAutoRoundingScale() : valueDecimal.toString();
					list.add(valueAsString);
				}
			} else {
				list.add(value.toString());
			}
		} else {
			list.add("");
		}
		return list;
	}

	protected List<String> addEmptyValue(final Integer howMany, final String text, final List<String> list) {
		for (int i = 0; i < howMany - 1; i++) {
			list.add("");
		}
		list.add(text);
		return list;
	}

	public R[] getSelected() {
		return selected;
	}

	public void setSelected(final R[] selected) {
		this.selected = selected;
	}

	public List<R> getSelectedRowList() {
		return Arrays.asList(selected);
	}

	public ArrayList<Long> getSelectedIds() {
		ArrayList<Long> ids = new ArrayList<Long>();
		if (selected != null)
			for (R row : selected) {
				ids.add(row.getId());
			}
		ids.addAll(getSelectedGridSetId());
		return ids;
	}

	public Boolean getExportOnlySelectedRow() {
		return exportOnlySelectedRow;
	}

	public void setExportOnlySelectedRow(final Boolean exportOnlySelectedRow) {
		this.exportOnlySelectedRow = exportOnlySelectedRow;
	}

	public void selectedLength() {
		if (selected == null) {
			exportOnlySelectedRow = Boolean.FALSE;
		} else if (selected.length < 1) {
			exportOnlySelectedRow = Boolean.FALSE;
		} else {
			exportOnlySelectedRow = Boolean.TRUE;
		}
	}

	public void setFilterValue(final String filterName, final Dictionary filterValue) {
		getFilters().get(filterName).setValue(filterValue);
		list.clear();
	}




	public AnalysisSource getAnalysisSource() {
		return analysisSource;
	}

	public void setAnalysisSourceForSale() {
		this.analysisSource = AnalysisSource.SALE;
	}

	public void setAnalysisSourceForPurchase() {
		this.analysisSource = AnalysisSource.PURCHASE;
	}

	protected void addIdToArrearTypeRow(final ArrearTypeRow arrearTypeRow) {
		switch (arrearTypeRow.getType()) {
			case ALL:
				arrearTypeRow.setId(ArrearTypeProvider.ALL_ID);
				break;
			case IN_TIME:
				arrearTypeRow.setId(ArrearTypeProvider.IN_TIME_ID);
				break;
			case PAST:
				arrearTypeRow.setId(ArrearTypeProvider.PAST_ID);
				break;

		}
	}

	public Boolean checkboxIsSelected() {
		if (selected == null) {
			return Boolean.TRUE;
		} else if (selected.length < 1) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public boolean isGridItemChecked() {
		return selectedGridSet != null && selectedGridSet.size() > 0;
	}

	public String[] getGridSelected() {
		return selectedGridSet.toArray(new String[0]);
	}

	public void setGridSelected(final String[] gridSelected) {
		// mock
	}

	public void gridSelectedChange(final String element) {
		if (element != null) {
			if (selectedGridSet.contains(element)) {
				selectedGridSet.remove(element);
			} else {
				selectedGridSet.add(element);
			}
		}
	}

	public void resetSelectedRows() {
		this.selected = null;
		this.selectedGridSet = new LinkedHashSet<String>();
	}

	/**
	 * Resetuj zaznaczone wiersze w tabeli oraz dataGrid
	 */
	public void resetSelectedRows(final String componentId) {
		if (StringUtil.isNotEmpty(componentId)) {
			FacesContext context = FacesContext.getCurrentInstance();
			String componentFullId = getElementFullId(componentId, context);
			if (StringUtil.isNotEmpty(componentFullId)) {
				resetTable(componentFullId);
			}
		}
	}

	/**
	 * Pobierane z listy parametrów, nad p:ajax dodany <f:param name="componentList" value="lista_identyfikatorów"/>
	 */
	public void resetComponents() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		String value = params.get("componentsList");
		if (StringUtil.isNotEmpty(value)) {
			String[] ids = value.split(",");
			for (String id : ids) {
				String componentFullId = getElementFullId(id, context);
				if (StringUtil.isNotEmpty(componentFullId)) {
					PrimeFaces.current().resetInputs(componentFullId);
					PrimeFaces.current().ajax().update(componentFullId);
				}
			}
		}
	}

	/**
	 * Pobiera pełne id komponentu z podanym id i aktualizuje element
	 */
	public void resetComponent(final String componentId) {
		if (StringUtil.isNotEmpty(componentId)) {
			FacesContext context = FacesContext.getCurrentInstance();
			String componentFullId = getElementFullId(componentId, context);
			if (StringUtil.isNotEmpty(componentFullId)) {
				PrimeFaces.current().resetInputs(componentFullId);
				PrimeFaces.current().ajax().update(componentFullId);
			}
		}
	}

	/**
	 * Pobiera pełne id komponentu o podanym id np.saleDocumentTableDocumentAction, pełne id to mainForm:saleDocumentTableDocumentAction
	 */
	private String getElementFullId(final String id, final FacesContext context) {
		UIViewRoot root = context.getViewRoot();
		final UIComponent[] found = new UIComponent[1];
		root.visitTree(new FullVisitContext(context), new VisitCallback() {

			@Override
			public VisitResult visit(final VisitContext context, final UIComponent component) {
				if (component.getId().equals(id)) {
					found[0] = component;
					return VisitResult.COMPLETE;
				}
				return VisitResult.ACCEPT;
			}
		});

		if (found[0] != null) {
			return found[0].getClientId();
		}
		return null;
	}

	public void resetTable(final String tableId) {
		resetSelectedRows();
		PrimeFaces.current().resetInputs(tableId);
		PrimeFaces.current().ajax().update(tableId);
	}

	public int getSelectedCount() {
		return selected == null ? 0 : selected.length;
	}

	public int getSelectedGridSetCount() {
		return selectedGridSet == null ? 0 : selectedGridSet.size();
	}

	protected boolean isRememberFilters() {
		return false;
	}

	protected String getCurrentPageURL() {
		Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (request instanceof HttpServletRequest) {
			String pageName = ((HttpServletRequest) request).getRequestURL().toString();
			pageName = pageName.substring(pageName.lastIndexOf("/"), pageName.length()).replaceAll("/", "");
			return pageName.substring(0, pageName.lastIndexOf(".do") + ".do".length());
		}
		return "";
	}

	protected String getFilterPage() {
		return getCurrentPageURL() + "_" + this.getClass().getSimpleName();
	}

	protected void clearOperatorFilters() {
		//operatorContextWww.getCurrentFilters().remove(getFilterPage());
	}

	public void saveOperatorFilter() {
		SearchCriteria criteria = new SearchCriteria(localSearch);
		if (saveUserSearchCriteria()) {
			criteria = getSearchCriteriaObject();
		}
		saveOperatorFilter(criteria);
	}

	private void saveOperatorFilter(final SearchCriteria criteria) {
		String filterPage = getFilterPage();
		if (criteria != null && criteria.getFilters() != null && criteria.getFilters().size() > 0) {
			//operatorContextWww.getCurrentFilters().put(filterPage, new HashMap<String, Filter>(criteria.getFilters()));
		}
	}

	public List<? extends AbstractRow> getPageList(final List<? extends AbstractRow> list) {
		int first = getSearchCriteriaObject() == null ? 0 : getSearchCriteriaObject().getFirst();
		int pageSize = getPageSize();
		int to = first + pageSize;
		if (list.size() < to) {
			first = 0;
			to = list.size();
		}
		List<? extends AbstractRow> subList = list.subList(first, to);
		return subList;
	}

	public List<List<String>> getSummaryUnderTable() {
		return summaryUnderTable;
	}

	public void setSummaryUnderTable(final List<List<String>> summaryUnderTable) {
		this.summaryUnderTable = summaryUnderTable;
	}

	public void setSummaryList(final List<List<String>> summaryUnderTable, final int emptyRowsLeft, final int emptyRowsRight) {
		summaryList.clear();
		int index = 0;
		for (List<String> list : summaryUnderTable) {
			List<String> tempList = new ArrayList<String>();
			tempList = addEmptyValue(emptyRowsLeft, index == 0 ? "Podsumowanie" : "", tempList);
			for (String value : list) {
				tempList = addToList(value, tempList, false);
			}
			tempList = addEmptyValue(emptyRowsRight, "", tempList);
			summaryList.add(tempList);
			index++;
		}
	}

	public R getRow(final int index) {
		return getActiveList().get(index);
	}

	public void removeFilter(final String key) {
		if (getFilters().containsKey(key)) {
			getFilters().remove(key);
		}
	}

	protected boolean isSaveDefaultOperatorFilters() {
		return false;
	}

	protected void defaultOperatorFilters(final Filters filters) {

	}

	protected Integer getFilterYear() {
		Integer value = (Integer) getFilters().get(Filter.YEAR_FILTER).getValue();
		return value;
	}

	protected void setYearFilter(final Integer year) {
		getFilters().get(Filter.YEAR_FILTER).setValue(year);
	}

	@Override
	public int count(Map<String, FilterMeta> map) {
		return getRowCount();
	}

	@Override
	public List<R> load(int i, int i1, Map<String, SortMeta> map, Map<String, FilterMeta> map1) {
		return null;
	}
}