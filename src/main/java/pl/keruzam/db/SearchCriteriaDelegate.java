package pl.keruzam.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import pl.keruzam.service.OperatorProfileService;
import pl.keruzam.service.cmm.OperatorProfileDto;
import pl.keruzam.service.cmm.Persistable;

/**
 * Zapamietywanie wartosci criteria w profilu operatora
 *
 * @author mirek
 */
@Named
@SessionScoped
public class SearchCriteriaDelegate extends Persistable implements Serializable {

	/**
	 * hack na problem z pageSizem TF-3611
	 */
	private static final CharSequence SALE_DOCUMENTS_CONTEXT = "T-pl.com.stream.topfirma.www.sale.SaleDocumentDataProvider-/views/sale/saleDocuments.do";

	OperatorProfileService operatorProfileService;
	Map<String, SearchCriteria> criteria = new HashMap<String, SearchCriteria>();

	SearchCriteriaDelegate() {

	}

	public SearchCriteriaDelegate(final OperatorProfileService operatorProfileService) {
		this.operatorProfileService = operatorProfileService;
	}

	public SearchCriteria getSearchCriteria(final Object target, final Long idCurrentCompany, final boolean isDicitionary) {
		Class<? extends Object> clazz = target.getClass();
		if (FacesContext.getCurrentInstance() != null) {
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			String context = "T-" + clazz.getName() + "-" + externalContext.getRequestServletPath() + "-" + idCurrentCompany;
			if (criteria.get(context) == null) {
				SearchCriteria searchCriteria = new SearchCriteria();
				if (!isDicitionary) {
					searchCriteria.setCriteriaString(operatorProfileService.getValue(context));
				}
				searchCriteria.setDicitionaryCriteria(isDicitionary);
				criteria.put(context, searchCriteria);
			}
			return criteria.get(context);
		}
		return null;
	}

	@Override
	public List<OperatorProfileDto> getPersistData() {
		List<OperatorProfileDto> list = new ArrayList<OperatorProfileDto>();
		for (String context : criteria.keySet()) {
			SearchCriteria searchCriteria = criteria.get(context);
			if (!searchCriteria.isDicitionaryCriteria()) {
				int pageSize = searchCriteria.getPageSize();
				// zabezpieczenie gdy na stronie dokumentow probuje na 1 strone wyswietlic wiecej niz 50 wynikow
				if (context.contains(SALE_DOCUMENTS_CONTEXT) && pageSize > 50) {
					pageSize = 50;
				}
				String value = "pageSize=" + pageSize;
				OperatorProfileDto dto = new OperatorProfileDto();
				dto.setKeyName(context);
				dto.setKeyValue(value);
				list.add(dto);
			}
		}
		return list;
	}
}
