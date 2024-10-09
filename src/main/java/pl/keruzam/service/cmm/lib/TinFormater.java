package pl.keruzam.service.cmm.lib;

import pl.keruzam.service.cmm.ConstCountry;

/**
 * Konwertuje numer NIP do czystej postaci (same cyfry bez dodatkowych znaków)
 * 
 * @author Tomasz Mazurek
 * 
 */
public class TinFormater {

	public static String formatPl(final String tinFormated) {
		return format(tinFormated, ConstCountry.POLSKA);
	}

	public static String format(final String tinFormated, final Long idCountry) {
		if (tinFormated != null) {
			if (ConstCountry.POLSKA.equals(idCountry)) {
				// tylko cyfry
				return tinFormated.replaceAll("\\D*", "");
			} else {
				return tinFormated.trim().replaceAll("-", "");
			}
		}
		return null;
	}

}
