package pl.keruzam.service;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;
import pl.keruzam.service.cmm.ContextHolder;
import pl.keruzam.service.cmm.PrintLang;

/**
 * Seriws do zasobow
 *
 * @author Mirek Szajowski
 */
@Component("resourceService")
public class ResourceService implements Serializable {

	private static final String CONVERTERS_LABEL_PATH_PL = "converters.label_PL";
	private static final String PRINT_LABEL_PATH_CZ = "prints.label_CZ";
	private static final String PRINT_LABEL_PATH_DE = "prints.label_DE";
	private static final String PRINT_LABEL_PATH_EN = "prints.label_EN";
	private static final String PRINT_LABEL_PATH_ES = "prints.label_ES";
	private static final String PRINT_LABEL_PATH_FR = "prints.label_FR";
	private static final String PRINT_LABEL_PATH_IT = "prints.label_IT";
	private static final String PRINT_LABEL_PATH_PL = "prints.label_PL";
	private static final String PRINT_LABEL_PATH_RU = "prints.label_RU";
	private static final String PRINT_LABEL_PATH_SK = "prints.label_SK";
	private static final String PRINT_LABEL_PATH_UA = "prints.label_UA";

	public String getMessage(final String key, final Object... params) {
		return ContextHolder.getContext().getMessage(key, params, Locale.ENGLISH);
	}

	public String getPrintLabel(final String key, final Integer idLanguage) {
		if (PrintLang.PL_LANG.equals(idLanguage)) {
			return getPrintLabelPL(key);
		} else if (PrintLang.EN_LANG.equals(idLanguage)) {
			return getPrintLabelEN(key);
		} else if (PrintLang.DE_LANG.equals(idLanguage)) {
			return getPrintLabelDE(key);
		} else if (PrintLang.FR_LANG.equals(idLanguage)) {
			return getPrintLabelFR(key);
		} else if (PrintLang.RU_LANG.equals(idLanguage)) {
			return getPrintLabelRU(key);
		} else if (PrintLang.IT_LANG.equals(idLanguage)) {
			return getPrintLabelIT(key);
		} else if (PrintLang.ES_LANG.equals(idLanguage)) {
			return getPrintLabelES(key);
		} else if (PrintLang.UA_LANG.equals(idLanguage)) {
			return getPrintLabelUA(key);
		} else if (PrintLang.CZ_LANG.equals(idLanguage)) {
			return getPrintLabelCZ(key);
		} else if (PrintLang.SK_LANG.equals(idLanguage)) {
			return getPrintLabelSK(key);
		}
		return getPrintLabelPL(key);
	}

	public String getConverterLabel(final String key) {
		return getLabel(key, CONVERTERS_LABEL_PATH_PL);
	}

	private String getPrintLabelPL(final String key) {
		return getLabel(key, PRINT_LABEL_PATH_PL);
	}

	private String getPrintLabelEN(final String key) {
		return getLabel(key, PRINT_LABEL_PATH_EN);
	}

	private String getPrintLabelDE(final String key) {
		return getLabel(key, PRINT_LABEL_PATH_DE);
	}

	private String getPrintLabelFR(final String key) {
		return getLabel(key, PRINT_LABEL_PATH_FR);
	}

	private String getPrintLabelRU(final String key) {
		return getLabel(key, PRINT_LABEL_PATH_RU);
	}

	private String getPrintLabelIT(final String key) {
		return getLabel(key, PRINT_LABEL_PATH_IT);
	}

	private String getPrintLabelES(final String key) {
		return getLabel(key, PRINT_LABEL_PATH_ES);
	}

	private String getPrintLabelUA(final String key) {
		return getLabel(key, PRINT_LABEL_PATH_UA);
	}

	private String getPrintLabelCZ(final String key) {
		return getLabel(key, PRINT_LABEL_PATH_CZ);
	}

	private String getPrintLabelSK(final String key) {
		return getLabel(key, PRINT_LABEL_PATH_SK);
	}

	private String getLabel(final String key, final String resourcePath) {
		ResourceBundle rb = ResourceBundle.getBundle(resourcePath);
		return rb.getString(key);
	}

}
