package pl.keruzam.service.cmm.lib;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import jakarta.persistence.Embeddable;

/**
 * Klasa służąca do operacji na liczbach zmiennoprzecinkowych. Zastępuje klasę BigDecimal. Obiekty tej klasy są obiektami niezmiennymi.
 *
 * @author Maciej Madajczyk
 */

@Embeddable
public class Decimal extends Number implements Serializable {

	/**
	 * Ilosc miejsc po przecinku dla Kwot (domyslnie 2)
	 */
	public static final int QUOTA_ROUNDING_SCALE = 2;
	public static final int QUANTITY_ROUNDING_SCALE = 4;
	public static final Decimal ZERO = Decimal.from("0");
	public static final Decimal ONE = Decimal.from("1");
	public static final Decimal TWO = Decimal.from("2");
	public static final Decimal TEN = Decimal.from("10");
	public static final Decimal HUNDRED = Decimal.from("100");
	public static final Decimal THOUSAND = Decimal.from("1000");
	/**
	 * Domyślny znak separatora części dziesiętnej.
	 */
	public static final char DEFAULT_DECIMAL_SEPARATOR = ',';
	public static final char DOT_SEPARATOR = '.';
	private static final int CURRENCY_ROUNDING_SCALE = 6;
	private static final String LOCAL_LANGUAGE = "pl";
	private static final String LOCAL_COUNTRY = "PL";
	private static final char ZERO_CHAR = '0';
	private static final char DEFAULT_GROUPING_SEPARATOR = ' ';
	/**
	 * wydruki jasper
	 */
	private static final char PRINT_DECIMAL_GROUPING_SEPARATOR = ' ';
	/**
	 * liczba którą opakowujemy
	 */
	private final BigDecimal bigDecimal;

	/**
	 * Dla serializacji
	 */
	public Decimal() {
		this(BigDecimal.ZERO);
	}

	public Decimal(final Integer val) {
		this(new BigDecimal(val));
	}

	public Decimal(final String val) {
		this(new BigDecimal(val.replaceAll(" ", "")));
	}

	public Decimal(final BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}

	public Decimal(final String val, final MathContext mc) {
		this(new BigDecimal(val, mc));
	}

	public Decimal(final double val) {
		this(new BigDecimal(val));
	}

	public Decimal(final double val, final MathContext mc) {
		this(new BigDecimal(val, mc));
	}

	/**
	 * Ustawienia lokalne
	 */
	public static Locale getLocale() {
		return new Locale(LOCAL_LANGUAGE, LOCAL_COUNTRY);
	}

	public static Decimal from(final String val) {
		return new Decimal(val);
	}

	public static Decimal from(final double val) {
		return new Decimal(val);
	}

	public static String trimStringValue(final String value) {
		return value.replaceAll(String.valueOf(DecimalFormatSymbols.getInstance(getLocale()).getGroupingSeparator()), "");
	}

	/**
	 * Sumuje ze soba wszystkie podane wartosci
	 */
	public static Decimal addAll(final Decimal... values) {
		Decimal sum = Decimal.ZERO;
		for (Decimal value : values) {
			sum = sum.add(value);
		}
		return sum;
	}

	public static Decimal getQuota(final Decimal quota) {
		return quota == null ? Decimal.ZERO : quota;
	}

	public static Decimal getQuota(final BigDecimal quota) {
		return quota == null ? Decimal.ZERO : new Decimal(quota);
	}

	public static Decimal addAllWithNullCheck(final Decimal... values) {
		Decimal sum = Decimal.ZERO;
		for (Decimal value : values) {
			sum = sum.add(getQuota(value));
		}
		return sum;
	}

	public static Decimal substractWithNullCheck(final Decimal value, final Decimal substractValue) {
		return getQuota(value).subtract(getQuota(substractValue));
	}

	/**
	 * pierwszy parametr wartosc od jakiej beda odejmowane kolejne parametry
	 */
	public static Decimal substractAllWithNullCheck(final Decimal substractFrom, final Decimal... values) {
		Decimal sum = getQuota(substractFrom);
		for (Decimal value : values) {
			sum = sum.subtract(getQuota(value));
		}
		return sum;
	}

	public static Decimal getPercent(final Decimal value) {
		return value == null ? null : value.multiply(HUNDRED);
	}

	public static Decimal getFromPercent(final Decimal value) {
		return value == null ? null : value.divide(HUNDRED, 4);
	}

	/**
	 * Modulo
	 */
	public static Decimal getRemainder(final Decimal value, final Decimal modulo) {
		return value == null ? null : new Decimal(value.getBigDecimal().multiply(modulo.getBigDecimal()).remainder(modulo.getBigDecimal()));
	}

	/**
	 * Zwraca źródłowy obiekt
	 */
	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}

	public Decimal getValue() {
		return new Decimal(bigDecimal);
	}

	/**
	 * zwraca wartosc float
	 */
	public float getFloatValue() {
		return this.bigDecimal.floatValue();
	}

	/**
	 * Dodaje podaną wartość i zwraca wynik w postaci nowej liczby.
	 */
	public Decimal add(final Decimal decimal) {
		BigDecimal b = decimal.getBigDecimal();
		BigDecimal sum = bigDecimal.add(b);
		return new Decimal(sum);
	}

	/**
	 * potegowanie
	 */
	public Decimal power(final int n) {
		BigDecimal result = bigDecimal.pow(n);
		return new Decimal(result);
	}

	public Decimal addWithNullCheck(final Decimal decimal) {
		if (decimal != null) {
			return add(decimal);
		}
		return this;
	}

	/**
	 * Dodaje podaną wartość i zwraca wynik w postaci nowej liczby.
	 */
	public Decimal add(final String val) {
		return add(new Decimal(val));
	}

	/**
	 * Odejmuje podaną wartość i zwraca wynik w postaci nowej liczby.
	 */
	public Decimal subtract(final Decimal decimal) {
		BigDecimal b = decimal.getBigDecimal();
		BigDecimal sub = bigDecimal.subtract(b);
		return new Decimal(sub);
	}

	/**
	 * Odejmuje podaną wartość i zwraca wynik w postaci nowej liczby.
	 */
	public Decimal subtract(final String val) {
		return subtract(new Decimal(val));
	}

	/**
	 * @return Zwraca ujemną wartosc jako 0
	 */
	public Decimal negativeZero() {
		if (Decimal.ZERO.isGreater(this)) {
			return Decimal.ZERO;
		}
		return this;
	}

	/**
	 * Mnoży o podaną wartość i zwraca wynik jako nową liczbę.
	 */
	public Decimal multiply(final Decimal decimal) {
		BigDecimal b = decimal.getBigDecimal();
		BigDecimal multiply = bigDecimal.multiply(b);
		return new Decimal(multiply);
	}

	/**
	 * Mnozy i zaokragla do dwoch miejsc po przecinku
	 */
	public Decimal multiplyQuota(final Decimal decimal) {
		return multiply(decimal, QUOTA_ROUNDING_SCALE);
	}

	/**
	 * Mnoży o podaną wartość i zwraca wynik jako nową liczbę.
	 */
	public Decimal multiply(final Decimal decimal, final int scale) {
		BigDecimal b = decimal.getBigDecimal();
		// to nie zaokrangla tylko skaluje
		// BigDecimal multiply = bigDecimal.multiply(b, new MathContext(scale,
		// RoundingMode.HALF_UP));
		BigDecimal multiply = bigDecimal.multiply(b);

		return new Decimal(multiply).round(scale);
	}

	/**
	 * Dzieli i zaokragla do dwoch miejsc po przecinku
	 */
	public Decimal divideQuota(final Decimal decimal) {
		return divide(decimal, QUOTA_ROUNDING_SCALE);
	}

	public Decimal divide(final Decimal decimal, final int scale) {
		BigDecimal b = decimal.getBigDecimal();
		BigDecimal divide = bigDecimal.divide(b, scale, RoundingMode.HALF_UP);
		return new Decimal(divide);
	}

	/**
	 * Zwraca wartość bezwzględną.
	 */
	public Decimal abs() {
		BigDecimal abs = bigDecimal.abs();
		return new Decimal(abs);
	}

	/**
	 * Zwraca reprezentację tekstową
	 */
	@Override
	public String toString() {
		return toFormatedStringQuota();
	}

	/**
	 * Metoda wykorzystywana przez pliki jasper do wyswietlania wartosci liczbowych na wydruku
	 */
	public String toPrintString() {
		return toFormatedPrintString(getLocale());
	}

	/**
	 * Porównuje z podaną liczbą.
	 *
	 * @param decimal
	 * @return -1 gdy mniejsza, 0 gdy równa , 1 gdy większa
	 */
	public int compareTo(final Decimal decimal) {
		BigDecimal b = decimal.getBigDecimal();
		return bigDecimal.compareTo(b);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Decimal) {
			return isEqual((Decimal) obj);
		}
		return false;
	}

	/**
	 * Zwraca czy jest większe od decimal.
	 */
	public boolean isGreater(final Decimal decimal) {
		return compareTo(decimal) > 0;
	}

	/**
	 * Zwraca czy jest większe od val.
	 */
	public boolean isGreater(final String val) {
		return compareTo(new Decimal(val)) > 0;
	}

	/**
	 * Zwraca czy jest większe od decimal lub równe decimal
	 */
	public boolean isGreaterOrEqual(final Decimal decimal) {
		return compareTo(decimal) >= 0;
	}

	/**
	 * Zwraca czy jest większe od decimal lub równe val
	 */
	public boolean isGreaterOrEqual(final String val) {
		return compareTo(new Decimal(val)) >= 0;
	}

	/**
	 * Zwraca czy jest równe decimal
	 */
	public boolean isEqual(final Decimal decimal) {
		return compareTo(decimal) == 0;
	}

	/**
	 * Zwraca czy jest równe val
	 */
	public boolean isEqual(final String val) {
		return compareTo(new Decimal(val)) == 0;
	}

	/**
	 * Zwraca czy jest mniejsze od decimal
	 */
	public boolean isLess(final Decimal decimal) {
		return compareTo(decimal) < 0;
	}

	/**
	 * Zwraca czy jest mniejsze od val
	 */
	public boolean isLess(final String val) {
		return compareTo(new Decimal(val)) < 0;
	}

	/**
	 * Zwraca czy jest mniejsze od decimal lub równe decimal
	 *
	 * @param decimal
	 * 		nie może być null
	 * @return
	 */
	public boolean isLessOrEqual(final Decimal decimal) {
		return compareTo(decimal) <= 0;
	}

	/**
	 * Zwraca czy jest mniejsze od decimal lub równe val
	 */
	public boolean isLessOrEqual(final String val) {
		return compareTo(new Decimal(val)) <= 0;
	}

	/**
	 * Podaje znak liczby.
	 *
	 * @return -1 ujemna, 0 zero, 1 dodatnia
	 */
	public int signum() {
		return bigDecimal.signum();
	}

	/**
	 * Zwraca czy jest równe zero
	 */
	public boolean isZero() {
		return signum() == 0;
	}

	/**
	 * Zwraca czy jest ujemne
	 */
	public boolean isNegative() {
		return signum() == -1;
	}

	/**
	 * Zwraca czy jest dodatnie
	 */
	public boolean isPositive() {
		return signum() == 1;
	}

	/**
	 * Porównuje this z decimal i zwraca obiekt który jest większy. Jeśli obiekty są równe zwraca this.
	 */
	public Decimal max(final Decimal decimal) {
		return compareTo(decimal) >= 0 ? this : decimal;
	}

	/**
	 * Porównuje this z decimal i zwraca obiekt który jest mniejszy. Jeśli obiekty są równe zwraca this.
	 */
	public Decimal min(final Decimal decimal) {
		return compareTo(decimal) <= 0 ? this : decimal;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		String number = this.toFormatedString();
		char[] charArray = number.toCharArray();
		StringBuilder sb = new StringBuilder();
		boolean separatorOccurs = false;
		for (int i = charArray.length - 1; i >= 0; i--) {
			char charAt = charArray[i];
			if ((ZERO_CHAR == charAt) && (!separatorOccurs)) {
				continue;
			}
			if (DEFAULT_DECIMAL_SEPARATOR == charAt) {
				separatorOccurs = true;
				continue;
			}
			sb.insert(0, charAt);
		}
		return sb.toString().hashCode();
	}

	/**
	 * Zaokrangla do podanej ilości miejsc po przecinku i zwraca wynik jako nowy obiekt. Orginalny obiekt nie jest zmieniany.
	 *
	 * @param scale
	 * 		liczba miejsc po przecinku
	 */
	public Decimal round(final int scale) {
		BigDecimal round = bigDecimal.setScale(scale, RoundingMode.HALF_UP);
		return new Decimal(round);
	}

	public Decimal round(final int scale, final RoundingMode mode) {
		BigDecimal round = bigDecimal.setScale(scale, mode);
		return new Decimal(round);
	}

	/**
	 * Zaokrangla kwotę do utalonej na stałe ilości miejsc po przecinku dla kwot
	 */
	public Decimal roundQuota() {
		BigDecimal round = bigDecimal.setScale(QUOTA_ROUNDING_SCALE, RoundingMode.HALF_UP);
		return new Decimal(round);
	}

	public Decimal roundQuantity() {
		BigDecimal round = bigDecimal.setScale(QUANTITY_ROUNDING_SCALE, RoundingMode.HALF_UP);
		return new Decimal(round);
	}

	/**
	 * Zaokrangla wartosc do utalonej na stałe ilości miejsc po przecinku dla kwot
	 */
	public Decimal roundCurrency() {
		BigDecimal round = bigDecimal.setScale(CURRENCY_ROUNDING_SCALE, RoundingMode.HALF_UP);
		return new Decimal(round);
	}

	public int getScale() {
		return bigDecimal.scale();
	}

	public Decimal negate() {
		return new Decimal(bigDecimal.negate());
	}

	/**
	 * 1 000 000.00 zostanie sformatowana do postaci 1000000,00
	 */
	public String toFormatedString() {
		return toFormatedString(getLocale(), DEFAULT_DECIMAL_SEPARATOR, false, 0);
	}

	/**
	 * Kwota dla API Firmino 1 000 000.00 -> 1000000.00
	 */
	public String toApiString() {
		return toFormatedString(getLocale(), DOT_SEPARATOR, false, 0);
	}

	/**
	 * Zwraca liczbę sformatowaną według następujących parametrów formatera.<br>
	 * <ul>
	 * <li>Domyślne locale</li>
	 * <li>separator dziesiętny: ,</li>
	 * <li>grupowanie części całkowitej po 3 znaki</li>
	 * </ul>
	 * Np. liczba <code>1000000.00</code> zostanie sformatowana do postaci <code>1 000 000,00</code>
	 */
	public String toGroupingFormatedString() {
		return toFormatedString(getLocale(), DEFAULT_DECIMAL_SEPARATOR, true, 3);
	}

	/**
	 * Zwraca liczbę sformatowaną według podanych parametrów formatera liczb dziesiętnych. Formatowanie obsługuje precyzję przechowywanej liczby.
	 *
	 * @param locale
	 * 		locale
	 * @param decimalSeparator
	 * 		separator części dziesiętnej
	 * @param groupingUsed
	 * 		czy grupować część całkowitą, np. jeżeli <code>true</code>, to liczba
	 * 		<code>1000000.0000</code> będzie wyświetlana w postaci <code>1 000 000.0000</code>
	 * @param groupingSize
	 * 		rozmiar grupy, np. dla 3 liczba <code>1000000.0000</code> będzie wyświetlana w postaci <code>1 000 000.0000</code>, natomiast dla 2 będzie
	 * 		<code>10 00 00 00.0000</code>
	 */
	public String toFormatedString(final Locale locale, final char decimalSeparator, final boolean groupingUsed, final int groupingSize) {
		return toFormatedString(locale, decimalSeparator, groupingUsed, groupingSize, getScale());
	}

	/**
	 * Wykonuje oprację: <b>this*10<sup>n</sup></b>
	 */
	public Decimal scaleByPowerOfTen(final int n) {
		return new Decimal(bigDecimal.scaleByPowerOfTen(n));
	}

	/**
	 * Zwraca liczbę sformatowaną według podanego formatera.
	 */
	public String toFormatedString(final NumberFormat numberFormat) {
		if (bigDecimal != null) {
			return numberFormat.format(bigDecimal);
		}
		return "";
	}

	public String toPlainString() {
		return bigDecimal.toPlainString();
	}

	@Override
	public double doubleValue() {
		return bigDecimal.doubleValue();
	}

	@Override
	public float floatValue() {
		return bigDecimal.floatValue();
	}

	@Override
	public int intValue() {
		return bigDecimal.intValue();
	}

	@Override
	public long longValue() {
		return bigDecimal.longValue();
	}

	@Override
	public byte byteValue() {
		return bigDecimal.byteValue();
	}

	/**
	 * Zwraca liczbę sformatowaną według podanych parametrów formatera liczb dziesiętnych. Formatowanie obsługuje precyzję przechowywanej liczby.
	 *
	 * @param locale
	 * 		locale
	 * @param decimalSeparator
	 * 		separator części dziesiętnej
	 * @param groupingUsed
	 * 		czy grupować część całkowitą, np. jeżeli <code>true</code>, to liczba
	 * 		<code>1000000.0000</code> będzie wyświetlana w postaci <code>1 000 000.0000</code>
	 * @param groupingSize
	 * 		rozmiar grupy, np. dla 3 liczba <code>1000000.0000</code> będzie wyświetlana w postaci <code>1 000 000.0000</code>, natomiast dla 2 będzie
	 * 		<code>10 00 00 00.0000</code>
	 * @param scale
	 * 		ilość znaków po separatorze
	 * @return
	 */
	public String toFormatedString(final Locale locale, final char decimalSeparator, final boolean groupingUsed, final int groupingSize, final int scale) {
		DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance(locale);
		numberFormat.setMinimumFractionDigits(scale);
		numberFormat.setMaximumFractionDigits(scale);
		numberFormat.setGroupingUsed(groupingUsed);
		numberFormat.setGroupingSize(groupingSize);
		DecimalFormatSymbols viewSymbols = numberFormat.getDecimalFormatSymbols();
		viewSymbols.setDecimalSeparator(decimalSeparator);
		viewSymbols.setGroupingSeparator(DEFAULT_GROUPING_SEPARATOR);
		numberFormat.setDecimalFormatSymbols(viewSymbols);
		return toFormatedString(numberFormat);
	}

	public String toFormatedPrintString(final Locale locale) {
		int scale = 2;
		int groupingSize = 3;
		boolean groupingUsed = true;
		DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance(locale);
		numberFormat.setMinimumFractionDigits(scale);
		numberFormat.setMaximumFractionDigits(scale);
		numberFormat.setGroupingUsed(groupingUsed);
		numberFormat.setGroupingSize(groupingSize);
		DecimalFormatSymbols viewSymbols = numberFormat.getDecimalFormatSymbols();
		viewSymbols.setDecimalSeparator(DEFAULT_DECIMAL_SEPARATOR);
		viewSymbols.setGroupingSeparator(PRINT_DECIMAL_GROUPING_SEPARATOR);
		numberFormat.setDecimalFormatSymbols(viewSymbols);
		return toFormatedString(numberFormat);
	}

	/**
	 * Zwraca liczbę sformatowaną według domyślnych parametrów formatera.<br>
	 * <ul>
	 * <li>Domyślne locale</li>
	 * <li>separator dziesiętny: ,</li>
	 * <li>grupowanie części całkowitej po 3 znaki</li>
	 * <li>liczba znaków po separatorze: 2</li>
	 * </ul>
	 * Np. liczba <code>1000000.00</code> zostanie sformatowana do postaci <code>1 000 000,00</code>
	 *
	 * @return
	 */
	public String toFormatedStringQuota() {
		return toFormatedString(getLocale(), DEFAULT_DECIMAL_SEPARATOR, true, 3, 2);
	}

	/**
	 * Formatowanie ilosci z 4 miejscami po przecinku
	 */
	public String toFormatedStringQuantity() {
		return toFormatedString(getLocale(), DEFAULT_DECIMAL_SEPARATOR, false, 0, 4);
	}

	/**
	 * Formatowanie ilosci bez miejsc po przecinku
	 */
	public String toFormatedStringQuotaRound() {
		return toFormatedString(getLocale(), DEFAULT_DECIMAL_SEPARATOR, true, 3, 0);
	}

	/**
	 * Zwraca liczbę sformatowaną według domyślnych parametrów formatera oprócz liczby znaków po separatorze<br>
	 * <ul>
	 * <li>Domyślne locale</li>
	 * <li>separator dziesiętny: ,</li>
	 * <li>grupowanie części całkowitej po 3 znaki</li>
	 * </ul>
	 *
	 * @param roundingScale
	 * 		ilość znaków po separatorze
	 */
	public String toFormatedStringQuota(final Integer roundingScale) {
		return toFormatedString(getLocale(), DEFAULT_DECIMAL_SEPARATOR, true, 3, roundingScale);
	}

	/**
	 * Zaokrangla do podanej ilości miejsc po przecinku zaokrąglając w dół i zwraca wynik jako nowy obiekt. Orginalny obiekt nie jest zmieniany. Np. liczba
	 * <code>5.6</code> zostanie sformatowana do postaci <code>5</code>
	 *
	 * @param scale
	 * 		liczba miejsc po przecinku
	 */
	public Decimal trim(final int scale) {
		BigDecimal round = bigDecimal.setScale(scale, RoundingMode.DOWN);
		return new Decimal(round);
	}

	/**
	 * Zwraca nowy {@code Decimal} który jest numerycznie równy this ale z usuniętymi końcowymi zerami.
	 */
	public Decimal stripTrailingZeros() {
		if (Decimal.ZERO.isEqual(this)) {
			return Decimal.ZERO;
		} else {
			return new Decimal(bigDecimal.stripTrailingZeros());
		}
	}

	/**
	 * Zwraca ilosc miejsc po przecinku
	 */
	public Integer numberOfPlacesBeyondTheDecimalPoint() {
		String value = bigDecimal.toString();
		int counted = 0;
		boolean isFirstNotZeroNumberExist = false;
		if (hasDecimalPoint(value)) {
			int indexOfDecimalPoint = indexOfDecimalPoint(value);
			String valueBeyondTheDecimalPoint = value.substring(indexOfDecimalPoint + 1, value.length());
			for (int i = valueBeyondTheDecimalPoint.length() - 1; i > -1; i--) {
				int number = Character.getNumericValue(valueBeyondTheDecimalPoint.toCharArray()[i]);
				if (number == 0 && isFirstNotZeroNumberExist) {
					counted++;
				}
				if (number != 0) {
					counted++;
					isFirstNotZeroNumberExist = true;
				}
			}
		}
		return counted;
	}

	private int indexOfDecimalPoint(final String value) {
		int index = value.indexOf(".");
		if (index > -1) {
			return index;
		}
		index = value.indexOf(",");
		return index;
	}

	private boolean hasDecimalPoint(final String value) {
		return value.contains(".") || value.contains(",");
	}

	/**
	 * Zwraca jako string z automatycznym wykryciem ilosci miejsc po przecinku (2 lub 4).
	 */
	public String toStringWithAutoRoundingScale() {
		int number = numberOfPlacesBeyondTheDecimalPoint();
		if (number > Decimal.QUOTA_ROUNDING_SCALE) {
			return roundQuantity().toFormatedString();
		} else {
			return roundQuota().toFormatedString();
		}
	}

	public String toPrintNoDigitsString() {
		int scale = 0;
		int groupingSize = 3;
		boolean groupingUsed = true;
		DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance(getLocale());
		numberFormat.setMinimumFractionDigits(scale);
		numberFormat.setMaximumFractionDigits(scale);
		numberFormat.setGroupingUsed(groupingUsed);
		numberFormat.setGroupingSize(groupingSize);
		DecimalFormatSymbols viewSymbols = numberFormat.getDecimalFormatSymbols();
		viewSymbols.setDecimalSeparator(DEFAULT_DECIMAL_SEPARATOR);
		viewSymbols.setGroupingSeparator(PRINT_DECIMAL_GROUPING_SEPARATOR);
		numberFormat.setDecimalFormatSymbols(viewSymbols);
		return toFormatedString(numberFormat);
	}

	/**
	 * Zwraca jako string z dopiskiem tyś jeśli kwota jest podzielna przez 1000 w przeciwnym wypadku zwraca normlaną wartośc, przylad 150000 = 150 tyś.
	 */
	public String toThousandFormattedString() {
		Decimal value = this;
		Decimal divideValue = value.divide(THOUSAND, 2);
		if (Decimal.ZERO.equals(divideValue.subtract(divideValue.round(0)))) {
			return divideValue.toPrintNoDigitsString() + " tyś";
		}
		return value.toPrintString();
	}

	public boolean isGreaterZero() {
		return isGreater(Decimal.ZERO);
	}

	/**
	 * Zwraca w postaci stringa wartośc z wartościami po przecinku. W zaleznosci od ilości miejsc, tyle cyfr po przecinku będzie wypisane.
	 */
	public String toFormatedValueWithAutoRoundingScale() {
		int numberOfPlacesBeyondTheDecimalPoint = numberOfPlacesBeyondTheDecimalPoint();
		return toFormatedStringQuota(numberOfPlacesBeyondTheDecimalPoint);
	}
}
