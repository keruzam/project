package pl.keruzam;

public class BankTransactionRow {
	String note;
	String quota;

	public BankTransactionRow(String note, String quota) {
		this.note = note;
		this.quota = quota;
	}

	public String getNote() {
		return note;
	}

	public String getQuota() {
		return quota;
	}
}
