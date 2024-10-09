package pl.keruzam.service.cmm.lib;

/**
 * Poziom porownania
 * 
 * @author Mirek Szajowski
 * 
 */
public enum ComparisonLevel {
	/**
	 * Rok
	 */
	Y("yyyy"),
	/**
	 * Rok,miesiac
	 */
	YM("yyyy-MM"),
	/**
	 * Rok,miesiac,dzien
	 */
	YMD("yyyy-MM-dd"),
	/**
	 * Godzina
	 */
	H("HH"),
	/**
	 * Godzina,minuta
	 */
	HM("HH:mm"),
	/**
	 * Godzina,minuta,sekunda
	 */
	HMS("HH:mm:ss"),
	/**
	 * Godzina,minuta,sekunda,milisekunda
	 */
	HMSM("HH:mm:ss:SSS"),
	/**
	 * Rok,miesiac,dzien,godzina
	 */
	YMDH("yyyy-MM-dd HH"),
	/**
	 * Rok,miesiac,dzien,godzina,minuta
	 */
	YMDHM("yyyy-MM-dd HH:mm"),
	/**
	 * Rok,miesiac,dzien,godzina,minuta,sekunda
	 */
	YMDHMS("yyyy-MM-dd HH:mm:ss"),
	/**
	 * Rok,miesiac,dzien,godzina,minuta,sekunda,milisekunda
	 */
	YMDHMSM("yyyy-MM-dd HH:mm:ss:SSS");

	private final String pattern;

	private ComparisonLevel(final String pattern) {
		this.pattern = pattern;
	}

	public String getComparePattern() {
		return pattern;
	}
}
