package pl.keruzam.service.cmm.lib;

/**
 * Obiekt Dat
 * 
 * @author Bartłomiej Jasik
 * 
 */
public class PeriodDate {

	private Date fromTime;
	private Date toTime;

	public PeriodDate(final Date fromTime, final Date toTime) {
		this.fromTime = fromTime;
		this.toTime = toTime;
	}

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(final Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getToTime() {
		return toTime;
	}

	public void setToTime(final Date toTime) {
		this.toTime = toTime;
	}

}
