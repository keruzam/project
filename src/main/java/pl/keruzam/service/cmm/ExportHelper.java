package pl.keruzam.service.cmm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.el.MethodExpression;
import jakarta.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;

/**
 * Obiekt przetrzymujący dane do exportu
 *
 * @author Bartek Jasik
 *
 */
public class ExportHelper {

	private static final Boolean PAGE_ONLY = Boolean.FALSE;
	List<List<String>> summaryList;
	private FacesContext context;
	private DataTable table;
	private String filename;
	private MethodExpression preProcessor;
	private MethodExpression postProcessor;
	private String author;
	private Map<String, String> filters;
	private Boolean selectionOnly = Boolean.FALSE;
	private String company;
	private String encodingType;
	private String title;
	private Integer footerStringValueHorizontalAlignment;
	private Integer footerNumericValueHorizontalAlignment;

	/**
	 * Klucz: indeks kolumny liczony od 0, wartość: com.itextpdf.text.Element
	 */
	private HashMap<Integer, Integer> columnsAlignmentsMap = new HashMap<Integer, Integer>();

	public FacesContext getContext() {
		return context;
	}

	public void setContext(final FacesContext context) {
		this.context = context;
	}

	public DataTable getTable() {
		return table;
	}

	public void setTable(final DataTable table) {
		this.table = table;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public boolean isPageOnly() {
		return PAGE_ONLY;
	}

	public boolean isSelectionOnly() {
		return selectionOnly;
	}

	public void setSelectionOnly(final Boolean selectionOnly) {
		this.selectionOnly = selectionOnly;
	}

	public String getEncodingType() {
		return encodingType;
	}

	public void setEncodingType(final String encodingType) {
		this.encodingType = encodingType;
	}

	public MethodExpression getPreProcessor() {
		return preProcessor;
	}

	public void setPreProcessor(final MethodExpression preProcessor) {
		this.preProcessor = preProcessor;
	}

	public MethodExpression getPostProcessor() {
		return postProcessor;
	}

	public void setPostProcessor(final MethodExpression postProcessor) {
		this.postProcessor = postProcessor;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public Map<String, String> getFilters() {
		return filters;
	}

	public void setFilters(final Map<String, String> filters) {
		this.filters = filters;
	}

	public final List<List<String>> getSummaryList() {
		return summaryList;
	}

	public final void setSummaryList(final List<List<String>> summaryList) {
		this.summaryList = summaryList;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(final String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public Integer getFooterStringValueHorizontalAlignment() {
		return footerStringValueHorizontalAlignment;
	}

	public void setFooterStringValueHorizontalAlignment(final int footerStringValueHorizontalAlignment) {
		this.footerStringValueHorizontalAlignment = footerStringValueHorizontalAlignment;
	}

	public Integer getFooterNumericValueHorizontalAlignment() {
		return footerNumericValueHorizontalAlignment;
	}

	public void setFooterNumericValueHorizontalAlignment(final int footerNumericValueHorizontalAlignment) {
		this.footerNumericValueHorizontalAlignment = footerNumericValueHorizontalAlignment;
	}

	public HashMap<Integer, Integer> getColumnsAlignmentsMap() {
		return columnsAlignmentsMap;
	}

	public void setColumnsAlignmentsMap(HashMap<Integer, Integer> columnsAlignmentsMap) {
		this.columnsAlignmentsMap = columnsAlignmentsMap;
	}
}
