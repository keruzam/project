package pl.keruzam.service.cmm.lib;

/**
 * Procesor do analizy porownania dat
 * 
 * @author Mirek Szajowski
 * 
 */
public class DateComparisionProcesor {
	public static boolean isDateEquals(final Date dateOne, final Date dateTwo, final ComparisonLevel level) {
		if (dateOne == null && dateTwo == null) {
			return true;
		}

		if (dateOne == null || dateTwo == null) {
			return false;
		}

		if (level == ComparisonLevel.Y) {
			if (dateOne.getYear() == dateTwo.getYear()) {
				return true;
			}
		} else if (level == ComparisonLevel.YM) {
			if ((dateOne.getYear() == dateTwo.getYear()) && (dateOne.getMonth() == dateTwo.getMonth())) {
				return true;
			}
		} else if (level == ComparisonLevel.YMD) {
			if ((dateOne.getYear() == dateTwo.getYear()) && (dateOne.getMonth() == dateTwo.getMonth())
					&& (dateOne.getDay() == dateTwo.getDay())) {
				return true;
			}
		} else if (level == ComparisonLevel.H) {
			if (dateOne.getHour() == dateTwo.getHour()) {
				return true;
			}
		} else if (level == ComparisonLevel.HM) {
			if ((dateOne.getHour() == dateTwo.getHour()) && (dateOne.getMinute() == dateTwo.getMinute())) {
				return true;
			}
		} else if (level == ComparisonLevel.HMS) {
			if ((dateOne.getHour() == dateTwo.getHour()) && (dateOne.getMinute() == dateTwo.getMinute())
					&& (dateOne.getSecond() == dateTwo.getSecond())) {
				return true;
			}
		} else if (level == ComparisonLevel.HMSM) {
			if ((dateOne.getHour() == dateTwo.getHour()) && (dateOne.getMinute() == dateTwo.getMinute())
					&& (dateOne.getSecond() == dateTwo.getSecond()) && (dateOne.getMilisecond() == dateTwo.getMilisecond())) {
				return true;
			}
		} else if (level == ComparisonLevel.YMDH) {
			if ((dateOne.getYear() == dateTwo.getYear()) && (dateOne.getMonth() == dateTwo.getMonth())
					&& (dateOne.getDay() == dateTwo.getDay() && (dateOne.getHour() == dateTwo.getHour()))) {
				return true;
			}
		} else if (level == ComparisonLevel.YMDHM) {
			if ((dateOne.getYear() == dateTwo.getYear()) && (dateOne.getMonth() == dateTwo.getMonth())
					&& (dateOne.getDay() == dateTwo.getDay() && (dateOne.getHour() == dateTwo.getHour()))
					&& (dateOne.getMinute() == dateTwo.getMinute())) {
				return true;
			}
		} else if (level == ComparisonLevel.YMDHMS) {
			if ((dateOne.getYear() == dateTwo.getYear()) && (dateOne.getMonth() == dateTwo.getMonth())
					&& (dateOne.getDay() == dateTwo.getDay() && (dateOne.getHour() == dateTwo.getHour()))
					&& (dateOne.getMinute() == dateTwo.getMinute()) && (dateOne.getSecond() == dateTwo.getSecond())) {
				return true;
			}
		} else if (level == ComparisonLevel.YMDHMSM) {
			if ((dateOne.getYear() == dateTwo.getYear()) && (dateOne.getMonth() == dateTwo.getMonth())
					&& (dateOne.getDay() == dateTwo.getDay() && (dateOne.getHour() == dateTwo.getHour()))
					&& (dateOne.getMinute() == dateTwo.getMinute()) && (dateOne.getSecond() == dateTwo.getSecond())
					&& (dateOne.getMilisecond() == dateTwo.getMilisecond())) {
				return true;
			}
		}
		return false;
	}
}
