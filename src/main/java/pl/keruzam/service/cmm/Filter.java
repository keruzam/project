package pl.keruzam.service.cmm;

import java.io.Serializable;

import pl.keruzam.service.cmm.lib.Dictionary;
import pl.keruzam.service.cmm.lib.FilterValueChange;
import pl.keruzam.service.cmm.lib.QueryOperator;
import pl.keruzam.service.cmm.lib.QueryValueArrayLogicalOperator;

/**
 * Filtr
 *
 * @author Mirek Szajowski
 */

public class Filter implements Serializable {

	public static final String KEY_YEAR_FILTER = "year";
	public static final String YEAR_FILTER = "docYear";
	public static final String MONTH_FILTER = "docYearMonth";
	public static final String QUARTER_FILTER = "docQuarter";
	public static final String DOCUMENT_FUNCTION = "documentFunction";
	public static final String TAX_SETTLEMENT_PERIOD = "taxSettlementPeriod";

	private String name;
	private Object value;
	private QueryOperator operator = QueryOperator.EQ;
	private FilterValueChange listener;
	private QueryValueArrayLogicalOperator queryValueArrayLogicalOperator = QueryValueArrayLogicalOperator.OR;

	public Filter() {

	}

	public Filter(final String name, final Object value) {
		this.name = name;
		if (value instanceof Long) {
			setValue(new Dictionary((Long) value));
		} else {
			setValue(value);
		}
	}

	public Filter(final String name, final Object value, final QueryOperator operator) {
		this(name, value);
		this.operator = operator;
	}

	public Filter(final String name, final Object value, final QueryOperator operator, final QueryValueArrayLogicalOperator logicalOperator) {
		this(name, value, operator);
		this.queryValueArrayLogicalOperator = logicalOperator;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(final Object value) {
		Object oldObject = this.value;
		if (Long.valueOf(0).equals(value)) { // org.apache.el.parser.
			// COERCE_TO_ZERO
			this.value = null;
		} else {
			this.value = value;
		}
		if (listener != null) {
			if (value != null) {
				if (!value.equals(oldObject)) {
					listener.valueChange();
				}
			} else if (oldObject != null) {
				if (!oldObject.equals(value)) {
					listener.valueChange();
				}
			}
		}
	}

	public QueryOperator getOperator() {
		return operator;
	}

	public void setOperator(final QueryOperator operator) {
		this.operator = operator;
	}

	public void setFilterValueChangeListener(final FilterValueChange listener) {
		this.listener = listener;
	}

	public QueryValueArrayLogicalOperator getQueryValueArrayLogicalOperator() {
		return queryValueArrayLogicalOperator;
	}

	public void setQueryValueArrayLogicalOperator(final QueryValueArrayLogicalOperator queryValueArrayLogicalOperator) {
		this.queryValueArrayLogicalOperator = queryValueArrayLogicalOperator;
	}

}
