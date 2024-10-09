package pl.keruzam.service.operator;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pl.keruzam.service.cmm.Filter;
import pl.keruzam.service.cmm.lib.SortOrder;
import pl.keruzam.service.cmm.stereotypes.DataProvider;

/**
 * DataProvider dla Operatora
 *
 * @author Tomasz Mazurek
 */

@Named
@ViewScoped
@DataProvider
public class OperatorDataProvider extends AbstractDataProvider<OperatorRow> {

	@Inject
	OperatorService operatorService;

	@Override
	protected List<OperatorRow> find(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final Filter[] filters,
			final String filter) {
		return operatorService.find(first, pageSize, sortField, sortOrder, filter);
	}

	@Override
	protected boolean isDicitionaryProvider() {
		return true;
	}

	public List<OperatorRow> getActiveOperators() {
		return getOperatorsByActive(Boolean.TRUE);
	}

	public List<OperatorRow> getInactiveOperators() {
		return getOperatorsByActive(Boolean.FALSE);
	}

	private List<OperatorRow> getOperatorsByActive(final Boolean isActive) {
		List<OperatorRow> activeList = getActiveList();
		List<OperatorRow> operators = new ArrayList<OperatorRow>();
		for (OperatorRow row : activeList) {
			if (isActive.equals(row.getIsActive())) {
				operators.add(row);
			}
		}
		return operators;
	}

}
