package pl.keruzam.service.cmm.lib;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.google.common.collect.Lists;
import jakarta.persistence.Embeddable;

/**
 * Klasa aktulanej daty.
 */
@Embeddable
public class Date extends PersistableObject implements Serializable {

	public static final List<Integer> MONTHS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

	public static final String YYYY = "yyyy";
	public static final String YYYY_MM = "yyyy-MM";
	public static final String MM_YYYY = "MM-yyyy";
	public static final String YYYY_WW = "yyyy-ww";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MMMM = "yyyy MMMM";
	public static final String MMMM_YYYY = "MMMM yyyy";
	public static final String DD_MM_YYYY = "dd-MM-yyyy";
	public static final String SHORT_DATE_PATTERN = "dd.MM.yy";
	public static final String LONG_DATE_PATTERN = "d MMMM yyyy";
	public static final String FULL_DATE_PATTERN = "EEEE, d MMMM yyyy";

	public static final Comparator<Date> COMPARATOR = new Comparator<Date>() {
		@Override
		public int compare(final Date o1, final Date o2) {
			return o1.compareTo(o2);
		}
	};
	/**
	 * Months numbers - natural order and numbers
	 */
	public static final int JANUARY = 1;
	public static final int FEBRUARY = 2;
	public static final int MARCH = 3;
	public static final int APRIL = 4;
	public static final int MAY = 5;
	public static final int JUNE = 6;

	public static final int JULY = 7;
	public static final int AUGUST = 8;
	public static final int SEPTEMBER = 9;
	public static final int OCTOBER = 10;
	public static final int NOVEMBER = 11;
	public static final int DECEMBER = 12;
	/**
	 * Ostatni dzień oststniego miesiąca
	 */
	public static final int LAST_DAY_OF_DECEMBER = 31;
	/**
	 * Data maksymalna <strong>2410-07-15</strong>
	 */
	public static final Date MAX_DATE = new Date(2410, JULY, 15);
	/**
	 * Data minimalna <strong>1899-12-30</strong>
	 */
	public static final Date MIN_DATE = new Date(1899, DECEMBER, 30);
	public static final long MILLISECS_PER_SECOND = 1000;
	public static final long MILLISECS_PER_MINUTE = MILLISECS_PER_SECOND * 60;
	public static final long MILLISECS_PER_HALF_HOUR = MILLISECS_PER_MINUTE * 30;
	public static final long MILLISECS_PER_HOUR = MILLISECS_PER_MINUTE * 60;
	public static final long MILLISECS_PER_DAY = MILLISECS_PER_HOUR * 24;
	public static final long MILLISECS_PER_WEEK = MILLISECS_PER_DAY * 7;
	public static final long MILLISECS_PER_MONTH = MILLISECS_PER_DAY * 30;
	public static final long MILLISECS_PER_YEAR = MILLISECS_PER_DAY * 365;
	public static final long MILLISECS_PER_180_DAYS = MILLISECS_PER_DAY * 180;
	public static final long MILLISECS_PER_360_DAY = MILLISECS_PER_DAY * 360;
	public static final int HALF_YEAR_IN_DAYS = 182;
	public static final List<Integer> QUARTER_FIRST = Lists.newArrayList(JANUARY, FEBRUARY, MARCH);
	public static final List<Integer> QUARTER_SECOND = Lists.newArrayList(APRIL, MAY, JUNE);
	public static final List<Integer> QUARTER_THIRD = Lists.newArrayList(JULY, AUGUST, SEPTEMBER);
	public static final List<Integer> QUARTER_FOURTH = Lists.newArrayList(OCTOBER, NOVEMBER, DECEMBER);
	private static final String ERROR_DATE_CANNOT_BE_NULL = "Date cannot be null";

	private static final int ONE_MINUTE = 60;

	public static Locale PL = new Locale("PL", "pl");

	/**
	 * Wewnętrzna wartość
	 */
	private java.util.Date value;

	private java.util.Date javaDate;

	/**
	 * Dla serializacji
	 */
	public Date() {
		Calendar instance = Calendar.getInstance();
		value = instance.getTime();
		javaDate = value;
	}

	/**
	 * Konstruktor
	 */
	public Date(final int year, final int month, final int day, final int hourOfDay, final int minute, final int second, final int milisecond) {
		Calendar instance = Calendar.getInstance();
		instance.set(year, month - 1, day, hourOfDay, minute, second);
		instance.set(Calendar.MILLISECOND, milisecond);
		value = instance.getTime();
		javaDate = value;
	}

	/**
	 * Konstruktor
	 */
	public Date(final int year, final int month, final int day, final int hourOfDay, final int minute, final int second) {
		Calendar instance = Calendar.getInstance();
		instance.set(year, month - 1, day, hourOfDay, minute, second);
		instance.set(Calendar.MILLISECOND, 0);
		value = instance.getTime();
		javaDate = value;
	}

	/**
	 * Konstruktor
	 */
	public Date(final int year, final int month, final int day) {
		Calendar instance = Calendar.getInstance();
		instance.set(year, month - 1, day, 0, 0, 0);
		instance.set(Calendar.MILLISECOND, 0);
		value = instance.getTime();
		javaDate = value;
	}

	/**
	 * Konstruktor
	 */
	public Date(final String miliseconds) {
		this(Long.valueOf(miliseconds));
	}

	/**
	 * Konstruktor bazujący na liczbie milisekund od początku epoki
	 */
	public Date(final Long dateMills, final boolean withTime) {
		this(withTime ? getDateWithoutMils(dateMills) : getDateWithoutTime(dateMills));
	}

	/**
	 * Konstruktor bazujący na java.util.Date
	 */
	public Date(final Long dateMills, final Precision precision) {
		this(precision == Precision.DATE_TIME_MILS ?
				new java.util.Date(dateMills) :
				precision == Precision.DATE_TIME ? getDateWithoutMils(dateMills) : getDateWithoutTime(dateMills));
	}

	/**
	 * Konstruktor bazujący na liczbie milisekund od początku epoki, nie resetuje milisekund
	 */
	public Date(final Long dateMills) {
		this(dateMills, Precision.DATE_TIME_MILS);
	}

	/**
	 * Konstruktor bazujący na java.util.Date
	 */
	public Date(final java.util.Date javaDate, final boolean withTime) {
		if (javaDate == null) {
			throw new IllegalArgumentException(ERROR_DATE_CANNOT_BE_NULL);
		}
		value = withTime ? getDateWithoutMils(javaDate.getTime()) : getDateWithoutTime(javaDate.getTime());
		this.javaDate = value;
	}

	/**
	 * Konstruktor bazujący na java.util.Date, nie resetuje milisekund
	 */
	public Date(final java.util.Date javaDate) {
		if (javaDate == null) {
			throw new IllegalArgumentException(ERROR_DATE_CANNOT_BE_NULL);
		}
		value = javaDate;
		this.javaDate = value;
	}

	/**
	 * Konstruktor bazujący na java.util.Date
	 */
	public Date(final java.util.Date javaDate, final Precision precision) {
		if (javaDate == null) {
			throw new IllegalArgumentException(ERROR_DATE_CANNOT_BE_NULL);
		}
		if (precision == Precision.DATE_TIME_MILS) {
			value = javaDate;
		} else if (precision == Precision.DATE_TIME) {
			value = getDateWithoutMils(javaDate.getTime());
		} else if (precision == Precision.DATE) {
			value = getDateWithoutTime(javaDate.getTime());
		} else {
			value = getDateWithoutTime(javaDate.getTime());
		}
		this.javaDate = value;
	}

	/**
	 * Czyści pola czasu w dacie
	 */
	private static java.util.Date getDateWithoutTime(final Long dateMills) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date(dateMills));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Czyści pole milisekund w dacie
	 */
	private static java.util.Date getDateWithoutMils(final Long dateMills) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date(dateMills));
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Porownuje daty dla zadanej preecyzji porownania
	 */
	public static boolean equals(final Date date1, final Date date2, final ComparisonLevel level) {
		return DateComparisionProcesor.isDateEquals(date1, date2, level);
	}

	/**
	 * Zwraca aktualną datę (pola czasu są wyzerowane)
	 */
	public static Date getCurrentDate() {
		// XXX: zastanowic sie skad ma byc ten czas z serwera czy bazy danych
		return new Date(new java.util.Date(), Precision.DATE);
	}

	/**
	 * Sprawdza czy aktualna data jest wieksza od podanej porownujac rok, miesiac i dzien
	 */
	public static Boolean isCurrentDateGreaterThanParam(final Date date) {
		return Date.getCurrentDate().compateTo(date, ComparisonLevel.YMD) > 0;
	}

	/**
	 * Sprawdza czy pierwsza data jest wieksza od drugiej porownujac rok, miesiac i dzien
	 */
	public static Boolean isFirstDateGreaterThanSecondCompareYMD(final Date first, final Date second) {
		return first.compateTo(second, ComparisonLevel.YMD) > 0;
	}

	/**
	 * Zwraca aktualną datę i czas bez milisekund
	 */
	public static Date getCurrentDateAndTime() {
		// XXX: zastanowic sie skad ma byc ten czas z serwera czy bazy danych
		return new Date(new java.util.Date(), Precision.DATE_TIME);
	}

	/**
	 * Zwraca aktualną datę i czas z milisekundami
	 */
	public static Date getCurrentDateAndTimeAndMills() {
		// XXX: zastanowic sie skad ma byc ten czas z serwera czy bazy danych
		return new Date(new java.util.Date(), Precision.DATE_TIME_MILS);
	}

	/**
	 * Zwraca ciąg różnicy czasu dwóch dat, jeżeli timeStart jest wiekszy od timeStop lub któryś z czasów jest nullem zwraca ciąg pusty, zwraca ciąg zgodnie z
	 */
	public static String getDeltaTimeString(final Date timeStart, final Date timeStop) {
		if (timeStart == null || timeStop == null || timeStart.isAfter(timeStop)) {
			return "";
		}
		long startTimeMills = timeStart.getTimeMills();
		long stopTimeMills = timeStop.getTimeMills();
		long delta = stopTimeMills - startTimeMills;
		if (delta <= 0) {
			return "";
		}
		return getDeltaTimeString(delta);
	}

	/**
	 * Zwraca ciąg różnicy czasu na podstawie różnicy wyrażonej w postaci liczby milisekund, zwraca rożnice jako ciąg zawierający godziny, minuty, jeżeli są to
	 * także sekundy, jeżeli są to także milisekundy np. 03:30 (trzy godziny 30 minut), 00:05:15 (pięc minut i 15 sekund), 00:00:00:100 (sto milisekund). W
	 * przypadku rónicy wiekszej niż 24 godziny zwraca liczbę godzin np. 345:30 (345 godzin i 30 minut), jeżeli delta jest mniejsza od zera lub null zwraca ciąg
	 * pusty
	 */
	public static String getDeltaTimeString(final Long delta) {
		if (delta == null) {
			return "";
		}
		if (delta <= 0) {
			return "";
		}
		long deltaTime = delta;

		StringBuilder b = new StringBuilder();
		long deltaHours = deltaTime / MILLISECS_PER_HOUR;
		b.append(String.format("%02d", deltaHours));
		deltaTime = deltaTime - (deltaHours * MILLISECS_PER_HOUR);

		long deltaMinuts = deltaTime / MILLISECS_PER_MINUTE;
		b.append(":");
		b.append(String.format("%02d", deltaMinuts));
		deltaTime = deltaTime - (deltaMinuts * MILLISECS_PER_MINUTE);

		long deltaSeconds = deltaTime / MILLISECS_PER_SECOND;
		if ((deltaSeconds > 0) || (deltaTime > 0)) {
			b.append(":");
			b.append(String.format("%02d", deltaSeconds));
			deltaTime = deltaTime - (deltaSeconds * MILLISECS_PER_SECOND);
		}

		long deltaMiliseconds = deltaTime;
		if (deltaMiliseconds > 0) {
			b.append(" ");
			b.append(String.format("%03d", deltaMiliseconds));
		}

		return b.toString();
	}

	/**
	 * Zwraca ciąg różnicy czasu na podstawie różnicy wyrażonej w postaci liczby milisekund, zwraca rożnice jako ciąg zawierający dni godziny, minuty, np. 03 h,
	 * 30 min, 05 min; np2. 03dni, 03h, 04min Jeżeli delta jest mniejsza od zera lub null zwraca ciąg pusty
	 */
	public static String getDeltaTimeStringWithTimeWords(final Long delta) {
		if (delta == null) {
			return "";
		}
		if (delta <= 0) {
			return "";
		}
		long deltaTime = delta;

		StringBuilder b = new StringBuilder();
		long deltaDay = deltaTime / MILLISECS_PER_DAY;
		if (deltaDay > 0) {
			b.append(String.format("%02d", deltaDay));
			deltaTime = deltaTime - (deltaDay * MILLISECS_PER_DAY);
			b.append(" d, ");

		}

		long deltaHours = deltaTime / MILLISECS_PER_HOUR;
		if (deltaHours > 0) {
			b.append(String.format("%02d", deltaHours));
			deltaTime = deltaTime - (deltaHours * MILLISECS_PER_HOUR);
			b.append(" h, ");
		}

		long deltaMinuts = deltaTime / MILLISECS_PER_MINUTE;
		if (deltaMinuts > 0) {
			b.append(String.format("%02d", deltaMinuts));
			b.append(" min");
		}

		return b.toString();

	}

	/**
	 * Zwraca ciąg różnicy dni dwóch dat, jeżeli dateStart jest wieksza od dateStop lub któraś z dat jest nullem zwraca ciąg pusty, zwraca rożnice jako ciąg
	 * zawierający ilość dni
	 */
	public static String getDeltaDateString(final Date dateStart, final Date dateStop) {
		if (dateStart == null || dateStop == null || dateStart.isAfter(dateStop)) {
			return "";
		}
		return String.valueOf(dateStart.getDaysDelta(dateStop));
	}

	/**
	 * Zwraca różnice dni dwóch dat
	 */
	public static int getDeltaDateInt(final Date dateStart, final Date dateStop) {
		if (dateStart == null || dateStop == null || dateStart.isAfter(dateStop)) {
			return 0;
		}
		return (int) dateStart.getDaysDelta(dateStop);
	}

	/**
	 * Zwraca rożnice dat jako ciąg zawierający ilość dni, jeżeli delta jest mniejsza od zera lub null zwraca ciąg pusty
	 */
	public static String getDeltaDateString(final Long delta) {
		if (delta == null) {
			return "";
		}
		if (delta <= 0) {
			return "";
		}
		return String.valueOf(delta / MILLISECS_PER_DAY);
	}

	/**
	 * Tworzy datę na podstawie podanego źródłą i formaty. po podbiciu na java17 zmiana z java.text na java.time
	 */
	public static Date from(final String source, final String pattern) {
		if (source != null) {
			DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(pattern, PL);
			if (isPatternWithTime(pattern)) {
				LocalDateTime localDateTime = LocalDateTime.parse(source, dateTimeFormat);
				Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
				java.util.Date javaUtilDate = java.util.Date.from(instant);
				return new Date(javaUtilDate);
			} else if (isPatternWithDays(pattern)) {
				LocalDate localDate = LocalDate.parse(source, dateTimeFormat);
				Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
				java.util.Date javaUtilDate = java.util.Date.from(instant);
				return new Date(javaUtilDate);
			} else {
				YearMonth yearMonth = YearMonth.parse(source, dateTimeFormat);
				return new Date(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
			}
		}
		return null;
	}

	private static boolean isPatternWithTime(String pattern) {
		return pattern.contains("HH");
	}

	private static boolean isPatternWithDays(String pattern) {
		return pattern.contains("d");
	}

	public static Integer countMonthDifferent(final Date first, final Date second) {
		return (first.getYear() * 12 + first.getMonth()) - (second.getYear() * 12 + second.getMonth());
	}

	public static Date getMinDate() {
		return new Date(1900, 1, 1);
	}

	/**
	 * Maksymalna data dla dokumentów narzucona przez deklaracje
	 */
	public static Date getMaxDate() {
		return new Date(2030, 1, 1);
	}

	/**
	 * Maksymalna data z komponentu p:calendar
	 */
	public static Date getMaxDateForDateComponent() {
		int maxYear = Date.getCurrentDate().getYear() + 10;
		return new Date(maxYear, 12, 31);
	}

	public static Integer getMonthNumber(final String monthName) {
		java.util.Date date;
		try {
			date = new SimpleDateFormat("MMMM", PL).parse(monthName);
		} catch (ParseException e) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static String getMonthName(final int month) {
		return switch (month) {
			case Date.JANUARY -> "styczeń";
			case Date.FEBRUARY -> "luty";
			case Date.MARCH -> "marzec";
			case Date.APRIL -> "kwiecień";
			case Date.MAY -> "maj";
			case Date.JUNE -> "czerwiec";
			case Date.JULY -> "lipiec";
			case Date.AUGUST -> "sierpień";
			case Date.SEPTEMBER -> "wrzesień";
			case Date.OCTOBER -> "październik";
			case Date.NOVEMBER -> "listopad";
			case Date.DECEMBER -> "grudzień";
			default -> "";
		};
	}

	public static String getMonthName2(final int month) {
		return switch (month) {
			case Date.JANUARY -> "styczniu";
			case Date.FEBRUARY -> "lutym";
			case Date.MARCH -> "marcu";
			case Date.APRIL -> "kwietniu";
			case Date.MAY -> "maju";
			case Date.JUNE -> "czerwcu";
			case Date.JULY -> "lipcu";
			case Date.AUGUST -> "sierpniu";
			case Date.SEPTEMBER -> "wrześniu";
			case Date.OCTOBER -> "październiku";
			case Date.NOVEMBER -> "listopadzie";
			case Date.DECEMBER -> "grudniu";
			default -> "";
		};
	}

	/**
	 * Sprawdzanie nachodzenia na siebie (ale nie przyleganie) okresow (czy 2 okres nachodzi na 1):<br>
	 * <b>okres 1 - (dateFrom1 : dateTo1)</b> i <br>
	 * <b>okres 2 - (dateFrom2 : dateTo2)</b>
	 *
	 * @param dateFrom1
	 * 		data od bazowa
	 * @param dateTo1
	 * 		data do bazowa
	 * @param dateFrom2
	 * 		data od okresu do sprawdzenia
	 * @param dateTo2
	 * 		data do okresu do sprawdzenia
	 * @return true gdy okres 2 nachodzi (ale nie przylega) na okres 1, false w przeciwnym razie
	 */
	public static boolean isOverlapWithoutAdjacent(final Date dateFrom1, final Date dateTo1, final Date dateFrom2, final Date dateTo2) {
		boolean isBothBeforeFrom = dateFrom2.isEqualsOrBefore(dateFrom1) && dateTo2.isEqualsOrBefore(dateFrom1);
		boolean isBothAfterTo = dateFrom2.isEqualsOrAfter(dateTo1) && dateTo2.isEqualsOrAfter(dateTo1);
		if (isBothBeforeFrom || isBothAfterTo) {
			return false;
		}
		return true;
	}

	/**
	 * Sprawdzanie nachodzenia na siebie okresow (czy 2 okres nachodzi na 1):<br>
	 * <b>okres 1 - (dateFrom1 : dateTo1)</b> i <br>
	 * <b>okres 2 - (dateFrom2 : dateTo2)</b>
	 *
	 * @param dateFrom1
	 * 		data od bazowa
	 * @param dateTo1
	 * 		data do bazowa
	 * @param dateFrom2
	 * 		data od okresu do sprawdzenia
	 * @param dateTo2
	 * 		data do okresu do sprawdzenia
	 * @return true gdy okres 2 nachodzi (i przylega) na okres 1, false w przeciwnym razie
	 */
	public static boolean isOverlapWithAdjacent(final Date dateFrom1, final Date dateTo1, final Date dateFrom2, final Date dateTo2) {
		boolean isBothBeforeFrom = dateFrom2.isBefore(dateFrom1) && dateTo2.isBefore(dateFrom1);
		boolean isBothAfterTo = dateFrom2.isAfter(dateTo1) && dateTo2.isAfter(dateTo1);
		return !isBothBeforeFrom && !isBothAfterTo;
	}

	public static int getFirstMonthNoOfQuarter(final int quarterNo) {
		return getQuarterMonthList(quarterNo).get(0);
	}

	public static int getLastMonthNoOfQuarter(final int quarterNo) {
		List<Integer> quarterMonthList = getQuarterMonthList(quarterNo);
		return quarterMonthList.get(quarterMonthList.size() - 1);
	}

	public static int getQuarterNoByMonth(final int month) {
		if (QUARTER_FIRST.contains(month)) {
			return 1;
		} else if (QUARTER_SECOND.contains(month)) {
			return 2;
		} else if (QUARTER_THIRD.contains(month)) {
			return 3;
		} else if (QUARTER_FOURTH.contains(month)) {
			return 4;
		}
		throw new IllegalStateException();
	}

	public static List<Integer> getQuarterMonthList(final int quarter) {
		if (1 == quarter) {
			return QUARTER_FIRST;
		} else if (2 == quarter) {
			return QUARTER_SECOND;
		} else if (3 == quarter) {
			return QUARTER_THIRD;
		} else if (4 == quarter) {
			return QUARTER_FOURTH;
		}
		throw new IllegalStateException();
	}

	public static Date getFirstDayOfNextQuarter(final Date date) {
		int prevQuarter = (date.getMonth() - 1) / 3 + 1;
		if (prevQuarter == 4) {
			return new Date(date.getYear() + 1, 1, 1);
		}
		return switch (prevQuarter) {
			case 3 -> new Date(date.getYear(), 10, 1);
			case 2 -> new Date(date.getYear(), 7, 1);
			case 1 -> new Date(date.getYear(), 4, 1);
			default -> throw new IllegalStateException();
		};
	}

	public static String getQuarterFormat(final int month) {
		if (month > 0 && month < 4) {
			return "I";
		} else if (month > 3 && month < 7) {
			return "II";
		} else if (month > 6 && month < 10) {
			return "III";
		} else {
			return "IV";
		}
	}

	public static boolean isEndMonthOfQuarter(final Date date) {
		int month = date.getMonth();
		return isQuaterMonth(month);
	}

	public static boolean isQuaterMonth(final int month) {
		return month % 3 == 0;
	}

	public static String toApiDateString(final Date value) {
		return value == null ? null : new SimpleDateFormat(YYYY_MM_DD).format(value.toJavaDate());
	}

	public static Date fromApiString(final String value) {
		return value == null ? null : Date.from(value, YYYY_MM_DD);
	}

	public static Date fromApiDateWithTimeString(final String value) {
		return value == null ? null : Date.from(value, "yyyy-MM-dd HH:mm");
	}

	public static int getNumberOfDays(final int year) {
		GregorianCalendar gc = new GregorianCalendar();
		if (gc.isLeapYear(year)) {
			return 366;
		}
		return 365;
	}

	public static String getMonthName3(final int month) {
		return switch (month) {
			case Date.JANUARY -> "stycznia";
			case Date.FEBRUARY -> "lutego";
			case Date.MARCH -> "marca";
			case Date.APRIL -> "kwietnia";
			case Date.MAY -> "maja";
			case Date.JUNE -> "czerwca";
			case Date.JULY -> "lipca";
			case Date.AUGUST -> "sierpnia";
			case Date.SEPTEMBER -> "września";
			case Date.OCTOBER -> "października";
			case Date.NOVEMBER -> "listopada";
			case Date.DECEMBER -> "grudnia";
			default -> "";
		};
	}

	public static Boolean isAnyDateBeforeDate(final Date mainDate, final Date... afterDates) {
		if (mainDate == null) {
			return Boolean.FALSE;
		}
		for (Date afterDate : afterDates) {
			if (afterDate != null && afterDate.isBefore(mainDate)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public static Date fromTimestamp(final Timestamp timestamp) {
		return timestamp != null ? new Date(timestamp.getTime()) : null;
	}

	/**
	 * Zwraca datę sformatowaną według podanego wzorca.
	 */
	public String toFormatedString(final String pattern) {
		if (isWeekOfYearPattern(pattern) && !isAllWeekInTheSameYear()) {
			return countWeekNumberBetweenYears();
		}
		return getFormattedStringFromPattern(pattern);
	}

	/**
	 * Zwraca aktualną wartość jako java.util.Date
	 */
	public java.util.Date toJavaDate() {
		return new java.util.Date(value.getTime());
	}

	/**
	 * Zwraca aktualną wartość jako java.util.Date
	 */
	public java.util.Date getJavaDate() {
		return javaDate;
	}

	/**
	 * Zwraca aktualną wartość jako java.util.Calendar
	 */
	public Calendar toCallendar() {
		return toCallendar(PL);
	}

	/**
	 * Zwraca aktualną wartość jako java.util.Calendar
	 */
	public Calendar toCallendar(final Locale locale) {
		Calendar instance = Calendar.getInstance(locale);
		instance.setTime(value);
		return instance;
	}

	/**
	 * Zwraca aktualną wartość jako java.sql.Date
	 */
	public java.sql.Date toSqlDate() {
		return new java.sql.Date(getTimeMills());
	}

	/**
	 * Zwraca aktualną wartość jako java.sql.Timestamp
	 */
	public Timestamp toSqlTimestamp() {
		return new Timestamp(getTimeMills());
	}

	/**
	 * Zwraca aktualną wartość jako ilość milisekund od początku epoki
	 */
	public long getTimeMills() {
		return value.getTime();
	}

	/**
	 * Zwraca true jeśli przekazana data jest równa aktualnej
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Date date) {
			return getTimeMills() == date.getTimeMills();
		}
		return false;
	}

	/**
	 * Porownuje daty dla zadanej preecyzji porownania
	 */
	public boolean equals(final Date date, final ComparisonLevel level) {
		return DateComparisionProcesor.isDateEquals(this, date, level);
	}

	/**
	 * Zwraca hashcode
	 */
	@Override
	public int hashCode() {
		return value.hashCode();
	}

	/**
	 * Zwraca czy aktualna data jest większa od przekazanej (występuje po niej na osi czasu)
	 */
	public boolean isAfter(final Date dateTime) {
		return value.after(dateTime.value);
	}

	/**
	 * Zwraca czy aktualna data jest mniejsza od przekazanej (występuje przed nią na osi czasu)
	 */
	public boolean isBefore(final Date dateTime) {
		return value.before(dateTime.value);
	}

	/**
	 * Zwraca czy aktualna data jest równa lub większa od przekazanej (występuje w tym samym miejscu lub po niej na osi czasu)
	 */
	public boolean isEqualsOrAfter(final Date date) {
		return equals(date) || isAfter(date);
	}

	/**
	 * Zwraca czy aktualna data jest równa lub mniejsza od przekazanej (występuje w tym samym miejscu lub przed nią na osi czasu)
	 */
	public boolean isEqualsOrBefore(final Date date) {
		return equals(date) || isBefore(date);
	}

	/**
	 * Czy przekazana data rozni sie tylko o jeden dzien od aktualnej
	 */
	public boolean isNeighbour(final Date date) {
		Date pom = null;
		if (isAfter(date)) {
			pom = date.addDay(1);
		} else {
			pom = date.addDay(-1);
		}
		return equals(pom);
	}

	/**
	 * Zwraca czy aktualna data jest pomiędzy dwoma przekazanymi datami, aktualna data musi być większa lub równa od d1 i mniejsza lub równa od d2
	 */
	public boolean isInClosedRange(final Date d1, final Date d2) {
		return isEqualsOrAfter(d1) && isEqualsOrBefore(d2);
	}

	/**
	 * Zwraca czy aktualna data jest pomiędzy dwoma przekazanymi datami, aktualna data musi być większa od d1 i mniejsza od d2
	 */
	public boolean isInOpenRange(final Date d1, final Date d2) {
		return isAfter(d1) && isBefore(d2);
	}

	/**
	 * Porównuje aktualną datę z przekazaną
	 */
	public int compareTo(final Date anotherDate) {
		return value.compareTo(anotherDate.value);
	}

	/**
	 * Porównuje aktualną datę z podaną na odpowiednim poziomie
	 */
	public int compateTo(final Date anotherDate, final ComparisonLevel level) {
		String comparePattern = level.getComparePattern();
		String thisFormatedString = toFormatedString(comparePattern);
		String anotherFormatedString = anotherDate.toFormatedString(comparePattern);
		return Date.from(thisFormatedString, comparePattern).compareTo(Date.from(anotherFormatedString, comparePattern));
	}

	/**
	 * Zwraca rok
	 */
	public int getYear() {
		return toCallendar().get(Calendar.YEAR);
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym nowym rokiem
	 */
	public Date setYear(final int year) {
		Calendar callendar = toCallendar();
		callendar.set(Calendar.YEAR, year);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca miesiąc numerowany od 1 do 12, (Date.JANUARY .. Date.DECEMBER)
	 */
	public int getMonth() {
		return toCallendar().get(Calendar.MONTH) + 1;
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym nowym miesiącem
	 */
	public Date setMonth(final int month) {
		Calendar callendar = toCallendar();
		callendar.set(Calendar.MONTH, month - 1);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca dzień miesiąca
	 */
	public int getDay() {
		return toCallendar().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym nowym dniem miesiąca
	 */
	public Date setDay(final int day) {
		Calendar callendar = toCallendar();
		callendar.set(Calendar.DAY_OF_MONTH, day);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca godzinę
	 */
	public int getHour() {
		return toCallendar().get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Zwraca nową instancję klasy z ustawioną nową godziną
	 */
	public Date setHour(final int hour) {
		Calendar callendar = toCallendar();
		callendar.set(Calendar.HOUR_OF_DAY, hour);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca dzień tygodnia
	 */
	public int getDayOfWeek() {
		return toCallendar().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Zwraca minutę
	 */
	public int getMinute() {
		return toCallendar().get(Calendar.MINUTE);
	}

	/**
	 * Zwraca nową instancję klasy z ustawioną nową minutą
	 */
	public Date setMinute(final int minute) {
		Calendar callendar = toCallendar();
		callendar.set(Calendar.MINUTE, minute);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca sekundę
	 */
	public int getSecond() {
		return toCallendar().get(Calendar.SECOND);
	}

	/**
	 * Zwraca nową instancję klasy z ustawioną nową sekundą
	 */
	public Date setSecond(final int second) {
		Calendar callendar = toCallendar();
		callendar.set(Calendar.SECOND, second);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca milisekundę
	 */
	public int getMilisecond() {
		return toCallendar().get(Calendar.MILLISECOND);
	}

	/**
	 * Zwraca nową instancję klasy z ustawioną nową milisekundą
	 */
	public Date setMilisecond(final int milisecond) {
		Calendar callendar = toCallendar();
		callendar.set(Calendar.MILLISECOND, milisecond);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nową instancję klasy z nowo ustawionymi polami
	 */
	public Date setDateFields(final int year, final int month, final int day) {
		Calendar callendar = toCallendar();
		callendar.set(Calendar.YEAR, year);
		callendar.set(Calendar.MONTH, month - 1);
		callendar.set(Calendar.DAY_OF_MONTH, day);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nową instancję klasy z nowo ustawionymi polami
	 */
	public Date setTimeFields(final int hourOfDay, final int minute, final int second) {
		return setTimeFields(hourOfDay, minute, second, 0);
	}

	/**
	 * Zwraca nową instancję klasy z nowo ustawionymi polami
	 */
	public Date setTimeFields(final int hourOfDay, final int minute, final int second, final int milisecond) {
		Calendar callendar = toCallendar();
		callendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		callendar.set(Calendar.MINUTE, minute);
		callendar.set(Calendar.SECOND, second);
		callendar.set(Calendar.MILLISECOND, milisecond);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nową instancję klasy zwiększając o podaną liczbę lat
	 */
	public Date addYear(final int amount) {
		Calendar callendar = toCallendar();
		callendar.add(Calendar.YEAR, amount);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nową instancję klasy zwiększając podaną liczbę miesięcy. Jeżeli nowy miesiąc nie ma takiego dnia to ustawia ostatni dzień miesiąca.
	 */
	public Date addMonth(final int amount) {
		Calendar callendar = toCallendar();
		callendar.add(Calendar.MONTH, amount);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nową instancję klasy zwiększając o podaną liczbę dni
	 */
	public Date addDay(final int amount) {
		Calendar callendar = toCallendar();
		callendar.add(Calendar.DAY_OF_MONTH, amount);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nową instancję klasy zwiększając o podaną liczbę godzin
	 */
	public Date addHour(final int amount) {
		Calendar callendar = toCallendar();
		callendar.add(Calendar.HOUR_OF_DAY, amount);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nową instancję klasy zwiększając o podaną liczbę minut
	 */
	public Date addMinute(final int amount) {
		Calendar callendar = toCallendar();
		callendar.add(Calendar.MINUTE, amount);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nową instancję klasy zwiększając o podaną liczbę sekund
	 */
	public Date addSecond(final int amount) {
		Calendar callendar = toCallendar();
		callendar.add(Calendar.SECOND, amount);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nową instancję klasy zwiększając o podaną liczbę sekund
	 */
	public Date addSecond(final long amount) {
		Calendar callendar = toCallendar();
		long minutes = amount / ONE_MINUTE;
		long rest = amount - ONE_MINUTE * minutes;
		callendar.add(Calendar.MINUTE, (int) minutes);
		callendar.add(Calendar.SECOND, (int) rest);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nową instancję klasy zwiększając o podaną liczbę milisekund
	 */
	public Date addMilisecond(final int amount) {
		Calendar callendar = toCallendar();
		callendar.add(Calendar.MILLISECOND, amount);
		return new Date(callendar.getTime());
	}

	/**
	 * Zwraca nr ostatniego dnia z jej aktualnego miesiąca
	 */
	public int getLastDayOfMonth() {
		Calendar calendar = toCallendar();
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Zwraca nr ostatniego dnia z jej aktualnego roku
	 */
	public int getLastDayOfYear() {
		Calendar calendar = toCallendar();
		return calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na ostatni dzień jej aktualnego miesiąca
	 */
	public Date getLastDayOfMonthDate() {
		return setDay(getLastDayOfMonth());
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na pierwszy dzień jej aktualnego tygodnia (poniedziałek)
	 */
	public Date getFirstDayOfWeekDate() {
		Integer day = getDayForCountDayOfWeek();
		return new Date(this.javaDate.getTime() - day * MILLISECS_PER_DAY);
	}

	@SuppressWarnings("deprecation")
	private Integer getDayForCountDayOfWeek() {
		int day = this.value.getDay() - 1;
		return day == -1 ? 6 : day;
	}

	public Date getLastDayOfWeekDate() {
		Integer day = getDayForCountDayOfWeek();
		return new Date((this.javaDate.getTime() - day * MILLISECS_PER_DAY) + 6 * MILLISECS_PER_DAY);
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na pierwszy dzień jej aktualnego miesiąca
	 */
	public Date getFirstDayOfMonthDate() {
		return setDay(1);
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na pierwszy dzień jego aktualnego kwartału
	 */
	public Date getFirstDayOfQuarterDate() {
		@SuppressWarnings("deprecation") int mod = this.javaDate.getMonth() / 3;
		int firstMonthOfQuarter = (mod * 3) + 1;
		return setMonth(firstMonthOfQuarter).setDay(1);
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na ostatni dzień jego aktualnego kwartału
	 */
	public Date getLastDayOfQuarterDate() {
		@SuppressWarnings("deprecation") int a = (this.javaDate.getMonth() / 3) + 1;
		int lastMonthOfQuarter = a * 3;
		int lastDay = setMonth(lastMonthOfQuarter).getLastDayOfMonth();
		return setMonth(lastMonthOfQuarter).setDay(lastDay);
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na ostatni dzień jego aktualnego kwartału
	 */
	public Date getLastDayOfPreviousQuarterDate() {
		@SuppressWarnings("deprecation") int a = (this.javaDate.getMonth() / 3) + 1;
		int lastMonthOfQuarter = (a * 3) - 3;
		@SuppressWarnings("deprecation") int b = this.javaDate.getYear() + 1900;
		if (lastMonthOfQuarter == 0) {
			lastMonthOfQuarter = 12;
			b -= 1;
		}
		int lastDay = setMonth(lastMonthOfQuarter).getLastDayOfMonth();
		return setMonth(lastMonthOfQuarter).setDay(lastDay).setYear(b);
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na pierwszy dzień jego poprzedniego kwartału
	 */
	public Date getFirstDayOfPreviousQuarterDate() {
		@SuppressWarnings("deprecation") int a = (this.javaDate.getMonth() / 3) + 1;
		int firstMonthOfQuarter = (a * 3) - 5;
		@SuppressWarnings("deprecation") int b = this.javaDate.getYear() + 1900;
		if (firstMonthOfQuarter < 0) {
			firstMonthOfQuarter = 10;
			b -= 1;
		}
		return setMonth(firstMonthOfQuarter).setDay(1).setYear(b);
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na pierwszy dzień jej poprzedniego miesiąca
	 */
	public Date getFirstDayOfPreviousMonthDate() {
		final long currentMonth = MILLISECS_PER_DAY * getLastDayOfPreviousMonth();
		Date previousMonthDate = new Date(this.javaDate.getTime() - currentMonth);
		return previousMonthDate.getFirstDayOfMonthDate();
	}

	private long getLastDayOfPreviousMonth() {
		Calendar calendar = getCalendarOneMonthBeforeCurrentDate();
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	private Calendar getCalendarOneMonthBeforeCurrentDate() {
		Calendar calendar = Calendar.getInstance(PL);
		calendar.setTime(value);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		return calendar;
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na ostatni dzień jej poprzedniego miesiąca
	 */
	public Date getLastDayOfPreviousMonthDate() {
		final long currentMonthDay = MILLISECS_PER_DAY * getLastDayOfPreviousMonth();
		Date previousMonthDate = new Date(this.javaDate.getTime() - currentMonthDay);
		return previousMonthDate.getLastDayOfMonthDate();
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na ostatni dzień jej poprzedniego roku
	 */
	public Date getLastDayOfPreviousYearDate() {
		final long currentYearDay = MILLISECS_PER_DAY * getLastDayOfYear();
		Date previousYearDate = new Date(new java.util.Date().getTime() - currentYearDay);
		return previousYearDate.getLastDayOfYearDate();
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na pierwszy dzień jej poprzedniego roku
	 */
	public Date getFirstDayOfPreviousYearDate() {
		final long currentYearDay = MILLISECS_PER_DAY * getLastDayOfYear();
		Date previousYearDate = new Date(this.javaDate.getTime() - currentYearDay);
		return previousYearDate.getFirstDayOfYearDate();
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na ostatni dzień jej poprzedniego tygodnia
	 */
	public Date getLastDayOfPreviousWeekDate() {
		Date previousWeekDate = new Date(new java.util.Date().getTime() - MILLISECS_PER_WEEK);
		return previousWeekDate.getLastDayOfWeekDate();
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na pierwszy dzień jej poprzedniego tygodnia
	 */
	public Date getFirstDayOfPreviousWeekDate() {
		Date previousWeekDate = new Date(new java.util.Date().getTime() - MILLISECS_PER_WEEK);
		return previousWeekDate.getFirstDayOfWeekDate();
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na pierwszy dzień jej aktualnego roku
	 */
	public Date getFirstDayOfYearDate() {
		return setMonth(1).setDay(1);
	}

	/**
	 * Zwraca nową instancję klasy z ustawionym dniem na ostatni dzień jej aktualnego roku
	 */
	public Date getLastDayOfYearDate() {
		return setMonth(DECEMBER).setDay(LAST_DAY_OF_DECEMBER);
	}

	/**
	 * Zwraca czy aktualna data jest na pierwszym dniu jej aktualnego miesiaca
	 */
	public boolean isFirstDayOfMonth() {
		return getDay() == 1;
	}

	/**
	 * Zwraca czy aktualna data jest na ostatnim dniu jej aktualnego miesiaca
	 */
	public boolean isCurrentDataLastDayOfMonth() {
		int lastDay = getLastDayOfMonth();
		return lastDay == getDay();
	}

	/**
	 * Zwraca czy aktualna data jest początkiem jej aktualnego roku
	 */
	public boolean isFirstDayOfYear() {
		return getDay() == 1 && getMonth() == JANUARY;
	}

	/**
	 * Zwraca czy aktualna data jest końcem jej aktualnego roku
	 */
	public boolean isCurrentDateLastDayOfYear() {
		return getMonth() == DECEMBER && isCurrentDataLastDayOfMonth();
	}

	/**
	 * Zwraca datę sformatowaną według stylu pośredniego w domyślnym locale.<br> Dla polskiego locale będzie to yyyy-MM-dd np. 2010-01-01
	 */
	@Override
	public String toString() {
		return getFormattedStringFromPattern(YYYY_MM_DD);
	}

	private String countWeekNumberBetweenYears() {
		int currentWeekNumber = getCurrentWeekNumber();
		if (isFirstWeekOfYear(currentWeekNumber)) {
			return getYear() + 1 + "-0" + currentWeekNumber;
		}
		return getYear() + "-" + currentWeekNumber;
	}

	private int getCurrentWeekNumber() {
		int previousWeekNumber = getPreviousWeekNumber();

		if (previousWeekNumber == 51) {
			return 52;
		} else {
			if (getDay() <= 28) {
				return 53;
			} else {
				return 1;
			}
		}
	}

	private int getPreviousWeekNumber() {
		Date date = new Date(value);
		date.addDay(-7);
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear() + 1900, date.getMonth() - 1, date.getDay());
		int weekNumber = cal.get(Calendar.WEEK_OF_YEAR);
		return weekNumber;
	}

	private boolean isFirstWeekOfYear(final int weekNumber) {
		return weekNumber == 1;
	}

	private boolean isAllWeekInTheSameYear() {
		int yearFirstDayOfWeek = getFirstDayOfWeekDate().getYear();
		int yearLastDayOfWeek = getLastDayOfWeekDate().getYear();
		return yearFirstDayOfWeek == yearLastDayOfWeek;
	}

	private boolean isWeekOfYearPattern(final String pattern) {
		return YYYY_WW.equals(pattern);
	}

	public String toFormatedLocalString(final String pattern) {
		return getFormattedStringFromPattern(pattern);
	}

	public String toMMMM_YYYYlikeInJava6() {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendText(ChronoField.MONTH_OF_YEAR, TextStyle.FULL_STANDALONE).appendPattern(" yyyy")
				.toFormatter(PL);
		return getLocalDateTimeFromValue().format(formatter);
	}

	public String toYYYY_MMMMlikeInJava6() {
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy ").appendText(ChronoField.MONTH_OF_YEAR, TextStyle.FULL_STANDALONE)
				.toFormatter(PL);
		return getLocalDateTimeFromValue().format(formatter);
	}

	/**
	 * d MMMM yyyy (1 styczeń 2010)
	 */
	public String toLongFormatedString() {
		return toFormatedString(LONG_DATE_PATTERN);
	}

	/**
	 * EEEE, d MMMM yyyy (Poniedziałek, 1 styczeń 2024)
	 */
	public String toFullFormatedString() {
		return getFormattedStringFromPattern(FULL_DATE_PATTERN);
	}

	/**
	 * dd.MM.yy (01.01.10)
	 */
	public String toShortFormatedString() {
		return getFormattedStringFromPattern(SHORT_DATE_PATTERN);
	}

	/**
	 * YYYY_MM_DD (2024-01-01)
	 */
	public String toMediumFormatedString() {
		return getFormattedStringFromPattern(YYYY_MM_DD);
	}

	/**
	 * metoda wprowadzona po przejsciu na java17 - wykorzystanie DateTimeFormattera
	 */
	private String getFormattedStringFromPattern(String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, PL);
		LocalDateTime localDateTime = getLocalDateTimeFromValue();
		return localDateTime.format(dateTimeFormatter);
	}

	/**
	 * metoda wprowadzona po przejsciu na java17 - pobieranie LocalDateTime z value
	 */
	private LocalDateTime getLocalDateTimeFromValue() {
		Instant instant = value.toInstant();
		ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
		return zonedDateTime.toLocalDateTime();
	}

	/**
	 * Zwraca ilość dni pomiędzy datami
	 */
	public long getDaysDelta(final Date endDate) {
		Calendar startCallendar = toCallendar();
		Calendar endCallendar = endDate.toCallendar();
		long endL = endCallendar.getTimeInMillis() + endCallendar.getTimeZone().getOffset(endCallendar.getTimeInMillis());
		long startL = startCallendar.getTimeInMillis() + startCallendar.getTimeZone().getOffset(startCallendar.getTimeInMillis());
		return (endL - startL) / MILLISECS_PER_DAY;
	}

	/**
	 * Zwraca czy data posiada ustawione jakiekolwiek pole od czasu
	 */
	public boolean isDateWithTime() {
		return (getHour() != 0) || (getMinute() != 0) || (getSecond() != 0) || (getMilisecond() != 0);
	}

	/**
	 * Sprawdza czy podany dzien jest dniem swiatecznym
	 *
	 * @param everySunday
	 * 		czy kazda niedziela ma byc uznawana za dzien swiateczny
	 */
	public boolean isHolyDay(final boolean everySunday) {
		int year = getYear();
		int month = getMonth();
		int day = getDay();
		int dayOfWeek = getDayOfWeek();
		int sunday = Calendar.SUNDAY;
		int lastDayOfMonth = getLastDayOfMonth();
		int saintNumber = 9;
		// tablica swiat
		int[][] dateSaint = { { 1, 1, 5, 5, 8, 11, 11, 12, 12 }, { 1, 6, 1, 3, 15, 1, 11, 25, 26 } };

		if (everySunday && dayOfWeek == sunday) {
			return true;
		}
		for (int i = 0; i < saintNumber; i++) {
			if ((dateSaint[0][i] == month) && (dateSaint[1][i] == day)) {
				// jezeli jest swieto Trzech Kroli 01.06 i jest rok < 2011 to
				// nie jest to dzien
				// wolny od pracy
				return (dateSaint[0][1] != month) || (dateSaint[1][1] != day) || (year >= 2011);
			}
		}
		// sprawdz czy jest wielkanoc
		int a = year % 19;
		int c = year % 100;
		int b = year / 100;
		int d = b / 4;
		int e = b % 4;
		int f = (b + 8) / 25;
		int g = (b - f + 1) / 3;
		int h = (19 * a + b - d - g + 15) % 30;
		int j = c / 4;
		int k = c % 4;
		int s = (32 + 2 * e + 2 * j - h - k) % 7;
		int u = (a + 11 * h + 22 * s) / 451;
		int x = (h + s - 7 * u + 114) / 31; // miesiac wielkanocy
		int y = ((h + s - 7 * u + 114) % 31) + 1; // niedziela wielkanocna

		// jezeli niedziela przypada ostatniego dnia miesiaca
		if (lastDayOfMonth == y) {
			if ((day == 1) && (x + 1 == month)) {
				return true;
			}
		} else {
			// poniedzialek
			if ((day == y + 1) && (x == month)) {
				return true;
			} else if (y == 31) {
				if ((day == 1) && (x + 1 == month)) {
					return true;
				}
			}
		}
		// sprawdz czy jest Boze Cialo
		Date date1 = new Date(year, x, y);
		date1 = date1.addDay(60); // tyle dni dzieli od wielkanocy Boze Cialo
		Date date2 = new Date(year, month, day);
		return date1.equals(date2);
	}

	public boolean isHolyDayOrSaturday() {
		return isHolyDay(true) || getDayOfWeek() == Calendar.SATURDAY;
	}

	/**
	 * Zwraca pierwszy poprzedni dzień roboczy lub jezeli podany dzien jest dniem roboczym to zwraca podany dzien
	 */
	public Date getFirstEarlierOrEqualsWorkingDay() {
		Calendar currentCallendar = toCallendar();
		Date date = new Date(getDateWithoutTime(currentCallendar.getTimeInMillis()));
		while (true) {
			if (date.isHolyDay(true) || date.getDayOfWeek() == Calendar.SATURDAY) {
				date = date.addDay(-1);
			} else {
				break;
			}
		}
		return date;
	}

	/**
	 * Zwraca pierwszy poprzedni dzień roboczy
	 */
	public Date getFirstEarlierWorkingDay() {
		Calendar currentCallendar = toCallendar();
		Date date = new Date(getDateWithoutTime(currentCallendar.getTimeInMillis()));
		date = date.addDay(-1);
		while (true) {
			if (date.isHolyDay(true) || date.getDayOfWeek() == Calendar.SATURDAY) {
				date = date.addDay(-1);
			} else {
				break;
			}
		}
		return date;
	}

	/**
	 * Zwraca pierwszy nastepny dzień roboczy lub jezeli podany dzien jest dniem roboczym to zwraca podany dzien
	 */
	public Date getFirstLaterOrEqualsWorkingDay() {
		Calendar currentCallendar = toCallendar();
		Date date = new Date(getDateWithoutTime(currentCallendar.getTimeInMillis()));
		while (true) {
			if (date.isHolyDay(true) || date.getDayOfWeek() == Calendar.SATURDAY) {
				date = date.addDay(1);
			} else {
				break;
			}
		}
		return date;
	}

	/**
	 * Czyści pola czasu w dacie
	 */
	public Date getDateWithoutTime() {
		Calendar calendar = clearTime();
		return new Date(calendar.getTime());
	}

	/**
	 * Czyści pola czasu w dacie i zwraca stringa
	 */
	public String getDateWithoutTimeString() {

		Calendar calendar = clearTime();
		Date date = new Date(calendar.getTime());
		return date.getYear() + "-" + prepareValue(date.getMonth()) + "-" + prepareValue(date.getDay());
	}

	private Calendar clearTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(value);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	/**
	 * Czyści pole milisekund w dacie
	 */
	public Date getDateWithoutMils() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(value);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Date(calendar.getTime());
	}

	/**
	 * Zwraca kolejny dzień po dniu zadeklarowanym.
	 */
	public Date nextDayDate() {
		return new Date(value.getTime() + MILLISECS_PER_DAY);
	}

	/**
	 * Zwraca czas bez daty.
	 */

	public String getTimeWithoutDate() {
		return prepareValue(getHour()) + ":" + prepareValue(getMinute());
	}

	private String prepareValue(final Integer value) {
		return value.toString().length() == 1 ? "0" + value : value.toString();
	}

	public PeriodDate getPeriodDate(final PeriodType type) {
		if (type.equals(PeriodType.ANY)) {
			return new PeriodDate(null, null);
		} else if (type.equals(PeriodType.CURRENT_MONTH)) {
			return new PeriodDate(getFirstDayOfMonthDate(), getLastDayOfMonthDate());
		} else if (type.equals(PeriodType.CURRENT_QUARTER)) {
			return new PeriodDate(getFirstDayOfQuarterDate(), getLastDayOfQuarterDate());
		} else if (type.equals(PeriodType.CURRENT_WEEK)) {
			return new PeriodDate(getFirstDayOfWeekDate(), getLastDayOfWeekDate());
		} else if (type.equals(PeriodType.CURRENT_YEAR)) {
			return new PeriodDate(getFirstDayOfYearDate(), getLastDayOfYearDate());
		} else if (type.equals(PeriodType.PREVIOUS_MONTH)) {
			return new PeriodDate(getFirstDayOfPreviousMonthDate(), getLastDayOfPreviousMonthDate());
		} else if (type.equals(PeriodType.PREVIOUS_QUARTER)) {
			return new PeriodDate(getFirstDayOfPreviousQuarterDate(), getLastDayOfPreviousQuarterDate());
		} else if (type.equals(PeriodType.PREVIOUS_WEEK)) {
			return new PeriodDate(getFirstDayOfPreviousWeekDate(), getLastDayOfPreviousWeekDate());
		} else if (type.equals(PeriodType.PREVIOUS_YEAR)) {
			return new PeriodDate(getFirstDayOfPreviousYearDate(), getLastDayOfPreviousYearDate());
		} else if (type.equals(PeriodType.LAST_7_DAYS)) {
			return new PeriodDate(getPrevious7day(), getCurrentDate());
		} else if (type.equals(PeriodType.LAST_30_DAYS)) {
			return new PeriodDate(getPrevious30day(), getCurrentDate());
		} else if (type.equals(PeriodType.LAST_180_DAYS)) {
			return new PeriodDate(getPrevious180day(), getCurrentDate());
		} else if (type.equals(PeriodType.LAST_360_DAYS)) {
			return new PeriodDate(getPrevious360day(), getCurrentDate());
		}
		return new PeriodDate(new Date(), new Date());
	}

	private Date getPrevious360day() {
		return new Date(getCurrentDate().getTimeMills() - MILLISECS_PER_360_DAY);
	}

	private Date getPrevious180day() {
		return new Date(getCurrentDate().getTimeMills() - MILLISECS_PER_180_DAYS);
	}

	private Date getPrevious30day() {
		return new Date(getCurrentDate().getTimeMills() - MILLISECS_PER_MONTH);
	}

	private Date getPrevious7day() {
		return new Date(getCurrentDate().getTimeMills() - MILLISECS_PER_WEEK);
	}

	@Override
	public String getAsString() {
		return toFormatedString(YYYY_MM_DD_HH_MM);
	}

	@Override
	public void createFromString(final String object) {
		Date from = from(object, YYYY_MM_DD_HH_MM);
		this.value = from.value;
		this.javaDate = from.javaDate;
	}

	public Date beginningDayTime() {
		return setHour(0).setMinute(0).setSecond(0).setMilisecond(0);
	}

	public Date endingDayTime() {
		return setHour(23).setMinute(59).setSecond(59).setMilisecond(999);
	}

	public boolean isDST() {
		return TimeZone.getDefault().inDaylightTime(value);
	}

	public XMLGregorianCalendar toXmlGregorianCalendarValue() throws DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(value);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	}

	public String toTimeFormatedString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(value);
	}

	public String toZoneFormatedString() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(value);
	}

	public String toAgreementFormatedString() {
		return new SimpleDateFormat("dd.MM.yyyy").format(value);
	}

	public String toApiDateString() {
		return value == null ? null : new SimpleDateFormat(YYYY_MM_DD).format(value);
	}

	public String toApiDateWithTimeString() {
		return value == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(value);
	}

	public String toPeriodAgoFormated() {
		Date date = Date.getCurrentDate();
		if (this.isBefore(date)) {
			return this.toString();
		}
		long daysDelta = this.getDaysDelta(date);
		if (daysDelta > 10) {
			return this.toString();
		}
		if (daysDelta == 0) {
			return "Dzisiaj";
		} else if (daysDelta == 1) {
			return "Wczoraj";
		}
		return daysDelta + " dni temu";
	}

	public String getStringDateByFormat(final String format) {
		return value == null ? null : new SimpleDateFormat(format).format(value);
	}

	public boolean isOnTheSameDay(final Date compareDate) {
		return 0 == this.compateTo(compareDate, ComparisonLevel.YMD);
	}

	/**
	 * Dokładność przechowywanej wartości
	 *
	 * @author krzysiek
	 */
	public enum Precision {
		DATE, DATE_TIME, DATE_TIME_MILS
	}

}
