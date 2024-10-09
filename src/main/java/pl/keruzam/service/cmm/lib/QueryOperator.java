package pl.keruzam.service.cmm.lib;

/**
 * 
 * @author Pawel Niedzielan
 * 
 */
public enum QueryOperator {

	EQ("="), EQ_LS("<="), EQ_GT(">="), NOT_EQ("!="), IN("in");

	private String value;

	QueryOperator(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
