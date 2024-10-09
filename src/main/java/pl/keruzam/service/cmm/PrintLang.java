package pl.keruzam.service.cmm;

/**
 * Dostepne jezyki tlumaczen
 * 
 * @author Tomasz Mazurek
 * 
 */
public class PrintLang {

	public static final Integer PL_LANG = 1;
	public static final Integer EN_LANG = 2;
	public static final Integer DE_LANG = 3;
	public static final Integer FR_LANG = 4;
	public static final Integer RU_LANG = 5;
	public static final Integer IT_LANG = 6;
	public static final Integer ES_LANG = 7;
	public static final Integer UA_LANG = 8;
	public static final Integer CZ_LANG = 9;
	public static final Integer SK_LANG = 10;

	public static Integer getPrintLangFromApi(final String lang) {
		if (lang != null) {
			if ("pl".equalsIgnoreCase(lang)) {
				return PL_LANG;
			} else if ("en".equalsIgnoreCase(lang)) {
				return EN_LANG;
			} else if ("de".equalsIgnoreCase(lang)) {
				return DE_LANG;
			} else if ("fr".equalsIgnoreCase(lang)) {
				return FR_LANG;
			} else if ("ru".equalsIgnoreCase(lang)) {
				return RU_LANG;
			} else if ("it".equalsIgnoreCase(lang)) {
				return IT_LANG;
			} else if ("es".equalsIgnoreCase(lang)) {
				return ES_LANG;
			} else if ("ua".equalsIgnoreCase(lang)) {
				return UA_LANG;
			} else if ("cz".equalsIgnoreCase(lang)) {
				return CZ_LANG;
			} else if ("sk".equalsIgnoreCase(lang)) {
				return SK_LANG;
			}
		}
		return PrintLang.PL_LANG;
	}

}
