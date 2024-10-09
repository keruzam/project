package pl.keruzam.service.cmm;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

/**
 * Dostawca typów zaległości
 *
 * @author Bartłomiej Jasik
 */
@Named
@ApplicationScoped
public class ArrearTypeProvider {

	public static final Long ALL_ID = 1L;
	public static final Long PAST_ID = 2L;
	public static final Long IN_TIME_ID = 3L;

	List<ArrearTypeRow> list = new ArrayList<ArrearTypeRow>();

	public List<ArrearTypeRow> getActiveList() {
		if (list.isEmpty()) {
			FacesContext context = FacesContext.getCurrentInstance();
			ResourceBundle bundle = context.getApplication().getResourceBundle(context, AbstractController.DEFAULT_MSG_BUNDLE);

			list.add(new ArrearTypeRow(bundle.getString("analysis.arrearTypeAll"), ArrearType.ALL, ALL_ID));
			list.add(new ArrearTypeRow(bundle.getString("analysis.arrearTypeInTime"), ArrearType.IN_TIME, IN_TIME_ID));
			list.add(new ArrearTypeRow(bundle.getString("analysis.arrearTypePast"), ArrearType.PAST, PAST_ID));
		}
		return list;
	}
}
